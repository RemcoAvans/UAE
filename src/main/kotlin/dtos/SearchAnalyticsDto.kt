package dtos

import kotlinx.serialization.Serializable

@Serializable
data class SearchAnalyticsDto(
    val totalSearches: Int,
    val averageResultsPerSearch: Double,
    val mostSearchedCategories: List<CategorySearchCount>,
    val mostSearchedCities: List<CitySearchCount>,
    val priceRangeDistribution: PriceRangeDistribution
)

@Serializable
data class CategorySearchCount(
    val category: String,
    val count: Int
)

@Serializable
data class CitySearchCount(
    val city: String,
    val count: Int
)

@Serializable
data class PriceRangeDistribution(
    val free: Int,           // 0
    val budget: Int,         // 1-10
    val moderate: Int,       // 11-25
    val premium: Int         // 25+
)
