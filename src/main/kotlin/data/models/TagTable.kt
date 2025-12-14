package com.example.data.models

import org.jetbrains.exposed.dao.id.IntIdTable

object TagTable : IntIdTable("tag") {
    val name = varchar("name", 255)
}
