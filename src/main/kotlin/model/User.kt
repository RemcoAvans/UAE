package model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val username: String,
    val email: String,
    val passwordHash: String,
    val role: String,
    val createdAt: LocalDate) {
}