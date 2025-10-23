package com.example.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class AnalyticsData(
    val id: Int,
    val locationId: Int,
    val activityCount: Int,
    val lastUpdated: LocalDate
)