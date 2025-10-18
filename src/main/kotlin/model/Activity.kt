package model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Activity (
    val id : Int,
    val title: String,
    val description: String,
    val photoUrl : String,
    val type : String,
    val price : Double,
    val createdByUserId: Int,
    val locationId : Int,
    val isHighlighted: Boolean = false,
    val capacity : Int,
    val isFull : Boolean,
    val StartDate: LocalDate,
    val EndDate: LocalDate,
    val RecurrencePattern: String,
    val RecurrenceDays: String,
    val createdAt: LocalDate,
    val imageUrl: String? = null,
    val isPublic: Boolean = true,
    val tags: List<String?> = emptyList(),
    val isFeatured: Boolean = false
){}
