package dtos

import kotlinx.serialization.Serializable

@Serializable
data class CategoryTrendDto(
    val category: String,
    val activityCount: Int,
    val averagePrice: Double,
    val searchCount: Int
)
