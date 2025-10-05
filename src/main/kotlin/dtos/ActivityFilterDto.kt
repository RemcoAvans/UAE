package dtos

@kotlinx.serialization.Serializable
data class ActivityFilterDto(
    val categories: List<String>? = null,
    val city: String? = null,
    val minPrice: Int? = null,
    val maxPrice: Int? = null,
    val dateFrom: String? = null,
    val dateTo: String? = null
)