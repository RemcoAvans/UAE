package com.example.data.models

import com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

object AnalyticsData : IntIdTable("analyticsData") {
    val locationId = integer("locationId")
    val activityCount = integer("activityCount")
    val lastUpdated = date("lastUpdated")
}