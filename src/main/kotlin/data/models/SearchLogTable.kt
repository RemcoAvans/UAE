package com.example.data.models

import org.jetbrains.exposed.dao.id.IntIdTable

object SearchLogTable : IntIdTable("searchLog"){
    val timestamp = long("timestamp")
//    val categories: List<String>?,
    val city = varchar("city", 255).nullable()
    val minPrice = integer("minPrice").nullable()
    val maxPrice = integer("maxPrice").nullable()
    val resultsCount = integer("resultsCount")
}
