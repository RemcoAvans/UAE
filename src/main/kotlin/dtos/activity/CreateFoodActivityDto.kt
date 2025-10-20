package dtos.activity

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
class CreateFoodActivityDto(
    override val title: String,
    override val description: String,
    override val type: String,
    override val price: Double,
    override val locationId: Int,
    override val capacity: Int,
    override val startDate: LocalDate,
    override val endDate: LocalDate,
    override val recurrencePattern: String,
    override val recurrenceDays: String,
    val Cuisine: String,
    val PriceRange: String,
) : CreateActivityDto<CreateFoodActivityDto>(
)  {
}