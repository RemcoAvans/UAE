package com.example.data.models

import org.jetbrains.exposed.dao.id.IntIdTable

object ActivityTable : IntIdTable("activities") {
    val title = varchar("title", 255)
    val description = varchar("description", 255)
    val photoUrl = varchar("photoUrl", 255)
    val type = varchar("type", 255)
    val price = double("price")
    val createdByUser = integer(name = "createdByUser")
    val locationId = integer("locationId")
    val isFeatured = bool("isFeatured")
    val capacity = integer("capacity").nullable()
    val startDate = varchar("startDate", 255).nullable()
    val endDate = varchar("endDate", 255).nullable()
    val recurrencePattern = varchar("recurrencePattern", 255).nullable()
    val recurrenceDays = varchar("recurrenceDays", 255).nullable()
    val phoneNumber = varchar("phoneNumber", 255).nullable()
    val createdAt = varchar("createdAt", 255)
}
