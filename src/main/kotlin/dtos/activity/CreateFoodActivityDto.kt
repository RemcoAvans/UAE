package dtos.activity

import kotlinx.datetime.LocalDate

class CreateFoodActivityDto(
    title: String,
    description: String,
    type: String,
    price: Double,
    locationId: Int,
    capacity: Int,
    startDate: LocalDate,
    endDate: LocalDate,
    recurrencePattern: String,
    recurrenceDays: String,
    val Cuisine: String,
    val PriceRange: String,
) : CreateActivityDto<CreateFoodActivityDto>(
    title,
    description,
    type,
    price,
    locationId,
    capacity,
    startDate,
    endDate,
    recurrencePattern,
    recurrenceDays
)  {
}