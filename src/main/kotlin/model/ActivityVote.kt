package com.example.model

import kotlinx.datetime.LocalDate

data class ActivityVote(
    val id: Int,
    val activityId: Int,
    val userId: Int,
    val voteType: String = "",
    val activityType: String = "",
    val tagSnapshot: String = "",
    val createAt: LocalDate = LocalDate.parse("2025-10-22"),
    val positive: Boolean,
) { }