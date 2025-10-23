package com.example.usecase.analytics

import com.example.core.ObjectResult
import com.example.usecase.BaseInputUseCase
import dtos.CategoryTrendDto
import repository.ActivityRepository
import repository.AnalyticsRepository

class GetActivityTrendsUseCase(
    private val activityRepository: ActivityRepository,
    private val analyticsRepository: AnalyticsRepository
) : BaseInputUseCase<Int?, List<CategoryTrendDto>> {

    override suspend fun execute(input: Int?): ObjectResult<List<CategoryTrendDto>> {
        val limit = input ?: 10 // Default top 10 categories

        val activities = activityRepository.getAll()
        val searchLogs = analyticsRepository.getAllSearchLogs()

        // Groepeer activities per category
        val categoryGroups = activities.groupBy { it.type }

        // Tel searches per category
        val categorySearchCount = mutableMapOf<String, Int>()
        searchLogs.forEach { log ->
            log.categories?.forEach { category ->
                categorySearchCount[category] = categorySearchCount.getOrDefault(category, 0) + 1
            }
        }

        // Maak trends per category
        val trends = categoryGroups.map { (category, activitiesInCategory) ->
            CategoryTrendDto(
                category = category,
                activityCount = activitiesInCategory.size,
                averagePrice = if (activitiesInCategory.isNotEmpty()) {
                    activitiesInCategory.map { it.price }.average()
                } else 0.0,
                searchCount = categorySearchCount.getOrDefault(category, 0)
            )
        }
            .sortedByDescending { it.searchCount + it.activityCount } // Sorteer op populariteit
            .take(limit)

        return if (trends.isEmpty()) {
            ObjectResult.notFound("Geen trends beschikbaar")
        } else {
            ObjectResult.success(trends)
        }
    }
}
