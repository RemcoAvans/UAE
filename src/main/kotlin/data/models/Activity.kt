package com.example.data.models

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.date
import kotlinx.datetime.LocalDate

object ActivityTable : IntIdTable("activities") {
    val title = varchar("title", 255)
    val description = varchar("description", 255)
    val photoUrl = varchar("photoUrl", 255)
    val type = varchar("type", 255)
    val price = double("price")
    val createdByUser = foreignKey()
    val locationId = foreignKey()
    val isFeatured = bool("isFeatured")
    val capacity = integer("capacity")
    val startDate = date("startDate")
    val endDate = date("endDate")
    val recurrencePattern = varchar("recurrencePattern", 255)
    val recurrenceDays = varchar("recurrenceDays", 255)
    val createdAt = date("createdAt")
}
