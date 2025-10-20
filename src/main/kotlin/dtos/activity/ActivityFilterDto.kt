package dtos.activity

import kotlinx.serialization.Serializable

@Serializable
data class ActivityFilterDto(
    val categories: List<String>? = null,
    val locationId: Int? = null,
    val minPrice: Int? = null,
    val maxPrice: Int? = null,
    val dateFrom: String? = null,
    val dateTo: String? = null
)