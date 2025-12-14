package com.example.data.models

import org.jetbrains.exposed.dao.id.IntIdTable

object SearchLogTable : IntIdTable("searchLog"){
    val timestamp = long("timestamp")
//    val categories: List<String>?,
    val city = varchar("city", 255)
    val minPrice = integer("minPrice")
    val maxPrice = integer("maxPrice")
    val resultsCount = integer("resultsCount")
}
