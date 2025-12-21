package com.example.data.models

import org.jetbrains.exposed.dao.id.IntIdTable

object LocationTable : IntIdTable("location"){
    val latitude = double("latitude")
    val longitude = double("longitude")
    val city = varchar("city", 255)
    val country = varchar("country", 255)
}
