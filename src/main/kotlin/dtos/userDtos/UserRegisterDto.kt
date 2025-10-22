package dtos


import com.example.config.PasswordHasher
import model.User
import kotlin.random.Random
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable


@Serializable
data class UserRegisterDto(
    val firstName: String,
    val lastName: String,
    val userName: String,
    val email: String,
    val password: String
)

fun UserRegisterDto.toUser(): User{
    val today: LocalDate = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date
    return User(
        id = Random.nextInt(0, Int.MAX_VALUE),
        firstName = this.firstName,
        lastName = this.lastName,
        username = this.userName,
        email = this.email,
        passwordHash = PasswordHasher.hash(this.password),
        role = "User",
        createdAt = today

    )

}

fun UserRegisterDto.toAdmin(): User{
    val today: LocalDate = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date
    return User(
        id = Random.nextInt(0, Int.MAX_VALUE),
        firstName = this.firstName,
        lastName = this.lastName,
        username = this.userName,
        email = this.email,
        passwordHash = PasswordHasher.hash(this.password),
        role = "Admin",
        createdAt = today

    )

}


