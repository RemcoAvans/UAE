package com.example.data.models

import org.jetbrains.exposed.dao.id.IntIdTable

object Tag : IntIdTable("tag") {
    val name = varchar("name", 255)
}
