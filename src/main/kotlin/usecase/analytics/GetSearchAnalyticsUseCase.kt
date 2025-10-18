package com.example.usecase.analytics

import com.example.core.ObjectResult
import dtos.*
import repository.AnalyticsRepository
import usecase.BaseUseCase

class GetSearchAnalyticsUseCase(
    private val analyticsRepository: AnalyticsRepository
) : BaseUseCase<SearchAnalyticsDto> {

    override suspend fun execute(): ObjectResult<SearchAnalyticsDto> {
        val searchLogs = analyticsRepository.getAllSearchLogs()

        if (searchLogs.isEmpty()) {
            return ObjectResult.notFound("Geen zoekgeschiedenis beschikbaar")
        }

        // Bereken gemiddeld aantal resultaten
        val averageResults = searchLogs.map { it.resultsCount }.average()

        // Tel categorieën
        val categoryCount = mutableMapOf<String, Int>()
        searchLogs.forEach { log ->
            log.categories?.forEach { category ->
                categoryCount[category] = categoryCount.getOrDefault(category, 0) + 1
            }
        }
        val mostSearchedCategories = categoryCount.entries
            .sortedByDescending { it.value }
            .take(10)
            .map { CategorySearchCount(it.key, it.value) }

        // Tel steden
        val cityCount = mutableMapOf<String, Int>()
        searchLogs.forEach { log ->
            log.city?.let { city ->
                cityCount[city] = cityCount.getOrDefault(city, 0) + 1
            }
        }
        val mostSearchedCities = cityCount.entries
            .sortedByDescending { it.value }
            .take(10)
            .map { CitySearchCount(it.key, it.value) }

        // Bereken prijsrange distributie
        var freeCount = 0
        var budgetCount = 0
        var moderateCount = 0
        var premiumCount = 0

        searchLogs.forEach { log ->
            when {
                log.maxPrice == 0 -> freeCount++
                (log.maxPrice ?: Int.MAX_VALUE) <= 10 -> budgetCount++
                (log.maxPrice ?: Int.MAX_VALUE) <= 25 -> moderateCount++
                else -> premiumCount++
            }
        }

        val analytics = SearchAnalyticsDto(
            totalSearches = searchLogs.size,
            averageResultsPerSearch = averageResults,
            mostSearchedCategories = mostSearchedCategories,
            mostSearchedCities = mostSearchedCities,
            priceRangeDistribution = PriceRangeDistribution(
                free = freeCount,
                budget = budgetCount,
                moderate = moderateCount,
                premium = premiumCount
            )
        )

        return ObjectResult.success(analytics)
    }
}
