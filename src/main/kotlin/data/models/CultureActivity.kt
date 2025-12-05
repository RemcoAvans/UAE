package com.example.data.models

import org.jetbrains.exposed.dao.id.IntIdTable

object CultureActivity : IntIdTable("CultureActivity") {
    val genre = varchar("genre", 255)
    val language = varchar("language", 255)
    val ageRestriction = integer("ageRestriction")
}