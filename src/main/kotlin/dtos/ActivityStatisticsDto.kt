package dtos

import kotlinx.serialization.Serializable

@Serializable
data class ActivityStatisticsDto(
    val totalActivities: Int,
    val publicActivities: Int,
    val privateActivities: Int,
    val averagePrice: Double,
    val mostUsedTags: List<TagCount>,
    val categoriesDistribution: List<CategoryCount>,
    val citiesDistribution: List<CityCount>
)

@Serializable
data class TagCount(
    val tag: String,
    val count: Int
)

@Serializable
data class CategoryCount(
    val category: String,
    val count: Int
)

@Serializable
data class CityCount(
    val city: String,
    val count: Int
)
