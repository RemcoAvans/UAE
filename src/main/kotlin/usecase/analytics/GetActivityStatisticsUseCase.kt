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

        // Bereken featured/normal verdeling (vervangt public/private)
        val featuredCount = activities.count { it.isFeatured }
        val normalCount = activities.size - featuredCount

        // Bereken gemiddelde prijs
        val averagePrice = activities.map { it.price }.average()

        // Tags zijn nu in een aparte tabel (ActivityTag), dus we geven een lege lijst terug
        // TODO: Implementeer tag statistics via ActivityTag repository
        val mostUsedTags = emptyList<TagCount>()

        // Bereken categorie distributie
        val categoryCount = activities.groupingBy { it.type }.eachCount()
        val categoriesDistribution = categoryCount.entries
            .sortedByDescending { it.value }
            .map { CategoryCount(it.key, it.value) }

        // Steden zijn nu via locationId (relatie met Location)
        // TODO: Implementeer cities statistics via Location repository
        val citiesDistribution = emptyList<CityCount>()

        val statistics = ActivityStatisticsDto(
            totalActivities = activities.size,
            publicActivities = featuredCount, // Nu: featured activities
            privateActivities = normalCount,  // Nu: normal activities
            averagePrice = averagePrice,
            mostUsedTags = mostUsedTags,
            categoriesDistribution = categoriesDistribution,
            citiesDistribution = citiesDistribution
        )

        return ObjectResult.success(statistics)
    }
}
