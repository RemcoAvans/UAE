package dtos.activity

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import model.Activity

@Serializable
abstract class CreateActivityDto<TCreateActivityDto>(
)
    where TCreateActivityDto : CreateActivityDto<TCreateActivityDto>
{
    abstract val title: String
    abstract val description: String
    abstract val type : String
    abstract val price : Double
    abstract val locationId : Int
    abstract val capacity : Int
    abstract val startDate: LocalDate
    abstract val endDate: LocalDate
    abstract val recurrencePattern: String
    abstract val recurrenceDays: String

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
            startDate = startDate,
            endDate = endDate,
            recurrencePattern = recurrencePattern,
            recurrenceDays = recurrenceDays,
            createdAt = Clock.System.now()
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .date
        )
}