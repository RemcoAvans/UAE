package model

import kotlinx.serialization.Serializable

@Serializable
data class SearchLog(
    val id: Int,
    val timestamp: Long,
    val categories: List<String>?,
    val city: String?,
    val minPrice: Int?,
    val maxPrice: Int?,
    val resultsCount: Int
)
