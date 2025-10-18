package com.example.usecase.analytics

import com.example.core.ObjectResult
import dtos.*
import repository.ActivityRepository
import usecase.BaseUseCase

class GetActivityStatisticsUseCase(
    private val activityRepository: ActivityRepository
) : BaseUseCase<ActivityStatisticsDto> {

    override suspend fun execute(): ObjectResult<ActivityStatisticsDto> {
        val activities = activityRepository.getAll()

        if (activities.isEmpty()) {
            return ObjectResult.notFound("Geen activiteiten beschikbaar")
        }

        // Bereken public/private verdeling
        val publicCount = activities.count { it.isPublic }
        val privateCount = activities.size - publicCount

        // Bereken gemiddelde prijs
        val averagePrice = activities.map { it.price }.average()

        // Tel tags
        val tagCount = mutableMapOf<String, Int>()
        activities.forEach { activity ->
            activity.tags.filterNotNull().forEach { tag ->
                tagCount[tag] = tagCount.getOrDefault(tag, 0) + 1
            }
        }
        val mostUsedTags = tagCount.entries
            .sortedByDescending { it.value }
            .take(15)
            .map { TagCount(it.key, it.value) }

        // Bereken categorie distributie
        val categoryCount = activities.groupingBy { it.type }.eachCount()
        val categoriesDistribution = categoryCount.entries
            .sortedByDescending { it.value }
            .map { CategoryCount(it.key, it.value) }

        // Bereken steden distributie (extraheer stad uit location)
        val cityCount = mutableMapOf<String, Int>()
        activities.forEach { activity ->
            // Extract city from location (bijv. "Dordrecht, Oude Haven" -> "Dordrecht")
            val city = activity.location.split(",").firstOrNull()?.trim()
            if (!city.isNullOrBlank()) {
                cityCount[city] = cityCount.getOrDefault(city, 0) + 1
            }
        }
        val citiesDistribution = cityCount.entries
            .sortedByDescending { it.value }
            .take(10)
            .map { CityCount(it.key, it.value) }

        val statistics = ActivityStatisticsDto(
            totalActivities = activities.size,
            publicActivities = publicCount,
            privateActivities = privateCount,
            averagePrice = averagePrice,
            mostUsedTags = mostUsedTags,
            categoriesDistribution = categoriesDistribution,
            citiesDistribution = citiesDistribution
        )

        return ObjectResult.success(statistics)
    }
}
