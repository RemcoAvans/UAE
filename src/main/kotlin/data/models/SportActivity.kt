package com.example.data.models

import org.jetbrains.exposed.dao.id.IntIdTable

object SportActivity: IntIdTable("sportActivity") {
    val isIndoor = bool("isIndoor")
    val difficulty = varchar("difficulty", 255)
    val equipmentRequired = bool("equipmentRequired")
}
