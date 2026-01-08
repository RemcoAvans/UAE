package model

import com.example.core.LocalDateSerializer
import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate

@Serializable
data class Activity(
    val id: Int,
    val title: String,
    val description: String,
    var photoUrl: String = "",
    val type: String,
    val price: Double,
    val createdByUserId: Int = 0,
    val locationId: Int,
    val isFeatured: Boolean = false,
    val capacity: Int,
    val isFull: Boolean,
    @Serializable(with = LocalDateSerializer::class) val startDate: LocalDate,
    @Serializable(with = LocalDateSerializer::class) val endDate: LocalDate,
    val recurrencePattern: String = "",
    val recurrenceDays: String = "",
    @Serializable(with = LocalDateSerializer::class) val createdAt: LocalDate? = null
)
