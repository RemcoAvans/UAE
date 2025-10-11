package dtos.activity

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import model.Activity

abstract class CreateActivityDto<TCreateActivityDto>(
    val title: String,
    val description: String,
    val type : String,
    val price : Double,
    val locationId : Int,
    val capacity : Int,
    val StartDate: LocalDate,
    val EndDate: LocalDate,
    val RecurrencePattern: String,
    val RecurrenceDays: String,
)
    where TCreateActivityDto : CreateActivityDto<TCreateActivityDto>
{
    fun toActivity(createdByUserId: Int): Activity =
        Activity(
            id = 0,
            title = title,
            description = description,
            photoUrl = "",
            type = type,
            price = price,
            createdByUserId = createdByUserId,
            locationId = locationId,
            isHighlighted = false,
            capacity = capacity,
            isFull = false,
            StartDate = StartDate,
            EndDate = EndDate,
            RecurrencePattern = RecurrencePattern,
            RecurrenceDays = RecurrenceDays,
            createdAt = Clock.System.now()
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .date
        )
}