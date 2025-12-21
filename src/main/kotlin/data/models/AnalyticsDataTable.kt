package com.example.data.models

import org.jetbrains.exposed.dao.id.IntIdTable

object AnalyticsDataTable : IntIdTable("analyticsData") {
    val locationId = integer("locationId")
    val activityCount = integer("activityCount")
    val lastUpdated = varchar("lastUpdated", 255)
}