package dtos.activity

import kotlinx.datetime.LocalDate

class CreateSportActivityDto(
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
    val isIndoor: Boolean,
    val difficulty: String,
    val equipmentRequired: Boolean,
) : CreateActivityDto<CreateSportActivityDto>(
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