package dtos

data class UserRegisterDto(
    val email: String,
    val passwordHash: String
)