package com.example.data.models

import org.jetbrains.exposed.dao.id.IntIdTable

object FoodActivityTable : IntIdTable("foodActivity") {
    val Cuisine = varchar("Cuisine", 255)
    val PriceRange = varchar("PriceRange", 255)
}
