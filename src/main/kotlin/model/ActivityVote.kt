package com.example.model

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class ActivityVote(
    val id: Int,
    val activityId: Int,
    val userId: Int,
    val createAt: LocalDate = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date,
    val positive: Boolean,
) { }