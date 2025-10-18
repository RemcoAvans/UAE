package com.example.model

import kotlinx.datetime.LocalDate

data class AnalyticsData(
    val id: Int,
    val locationId: Int,
    val activityCount: Int,
    val lastUpdated: LocalDate) {
}