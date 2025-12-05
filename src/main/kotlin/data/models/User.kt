package com.example.data.models

import org.jetbrains.exposed.dao.id.IntIdTable

object User : IntIdTable("user"){
    val firstName = varchar("firstName", 255)
    val lastName = varchar("lastName", 255)
    val username = varchar("username", 255)
    val email = varchar("email", 255)
    val passwordHash = varchar("passwordHash", 510)
    val role = varchar("role", 255)
    val createdAt = date()
}
