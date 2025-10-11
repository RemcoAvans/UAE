package dtos.activity

import kotlinx.datetime.LocalDate

class CreateCultureActivityDto(
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
    val genre: String,
    val language: String,
    val ageRestriction: Int,
) : CreateActivityDto<CreateCultureActivityDto>(
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
    ) {
}