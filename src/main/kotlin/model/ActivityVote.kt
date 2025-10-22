package com.example.model

import kotlinx.datetime.LocalDate

data class ActivityVote(
    val id: Int,
    val activityId: Int,
    val userId: Int,
    val voteType: String,
    val activityType: String,
    val tagSnapshot: String,
    val createAt: LocalDate,
    val positive: Boolean,
) { }