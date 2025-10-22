package com.example.dtos.activity

import com.example.core.LocalDateSerializer
import com.example.model.ActivityTag
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

data class ActivityDetailDto(
    val id: Int,
    val title: String,
    val description: String,
    val photoUrl: String = "",
    val type: String,
    val price: Double,
    val createdByUserId: Int = 0,
    val locationId: Int,
    val isHighlighted: Boolean = false,
    val capacity: Int,
    val isFull: Boolean,
    @Serializable(with = LocalDateSerializer::class) val startDate: LocalDate,
    @Serializable(with = LocalDateSerializer::class) val endDate: LocalDate,
    val recurrencePattern: String = "",
    val recurrenceDays: String = "",
    @Serializable(with = LocalDateSerializer::class) val createdAt: LocalDate? = null,
    val rating: Int,
    val tags: List<String>,
) {
}