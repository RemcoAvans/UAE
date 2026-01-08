package dtos.activity

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
class CreateFoodActivityDto(
    override val title: String,
    override val description: String,
    override val type: String,
    override val price: Double,
    override val locationLatitude: Double,
    override val locationLongitude: Double,
    override val capacity: Int? = null,
    override val startDate: LocalDate? = null,
    override val endDate: LocalDate? = null,
    override val recurrencePattern: String? = null,
    override val recurrenceDays: String? = null,
    val Cuisine: String,
    val PriceRange: String,
) : CreateActivityDto<CreateFoodActivityDto>(
)  {


}