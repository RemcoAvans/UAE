package dtos


import model.User
import java.util.Date
import kotlin.random.Random
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import java.security.MessageDigest

@Serializable
data class UserRegisterDto(
    val firstName: String,
    val lastName: String,
    val userName: String,
    val email: String,
    val password: String
)
fun String.sha256(): String {
    val bytes = this.toByteArray()
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(bytes)
    return digest.joinToString("") { "%02x".format(it) }
}
fun UserRegisterDto.toUser(): User{
    val today: LocalDate = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date
    return User(
        id = Random.nextInt(),
        firstName = this.firstName,
        lastName = this.lastName,
        username = this.userName,
        email = this.email,
        passwordHash = this.password.sha256() ,
        role = "user",
        createdAt = today

    )

}