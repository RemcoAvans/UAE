package routing

import dtos.UserRegisterDto
import com.example.dtos.userDtos.userLoginDto
import routing.userRoutes
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation as ClientContentNegotiation
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation as ServerContentNegotiation
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json
import model.User
import org.junit.Test
import repository.UserRepository
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class UserRoutesIntegrationTest {

    // Fake repository voor testing
    class FakeUserRepository(
        private val users: MutableList<User> = mutableListOf()
    ) : repository.UserRepository() {

        override suspend fun getAll(): List<User> = users

        override suspend fun getById(id: Int): User? =
            users.find { it.id == id }

        override suspend fun getByQuery(predicate: (User) -> Boolean): List<User> {
            return users.filter(predicate)
        }

        override suspend fun create(entity: User): User {
            users.add(entity)
            return entity
        }

        override suspend fun update(id: Int, entity: User): User? {
            val index = users.indexOfFirst { it.id == id }
            return if (index != -1) {
                users[index] = entity
                entity
            } else null
        }

        override suspend fun delete(id: Int): Boolean =
            users.removeIf { it.id == id }

        fun addUser(user: User) {
            users.add(user)
        }

        fun clearUsers() {
            users.clear()
        }

        fun getUsers(): List<User> {
            return users.toList()
        }
    }

    @Test
    fun `GET users - should return 200 with all users`() = testApplication {
        // Arrange
        val fakeRepo = FakeUserRepository()

        fakeRepo.addUser(User(
            id = 1,
            firstName = "John",
            lastName = "Doe",
            username = "johndoe",
            email = "john@example.com",
            passwordHash = "hash123",
            role = "User",
            createdAt = LocalDate.parse("2025-01-01")
        ))
        fakeRepo.addUser(User(
            id = 2,
            firstName = "Jane",
            lastName = "Smith",
            username = "janesmith",
            email = "jane@example.com",
            passwordHash = "hash456",
            role = "Admin",
            createdAt = LocalDate.parse("2025-01-02")
        ))

        application {
            install(ServerContentNegotiation) {
                json()
            }
            routing {
                userRoutes(fakeRepo)
            }
        }

        val client = createClient {
            install(ClientContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }

        // Act
        val response = client.get("/users")

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)

        val users = response.body<List<User>>()
        assertEquals(2, users.size)
    }

    @Test
    fun `GET users - should return 200 with empty list when no users exist`() = testApplication {
        // Arrange
        val fakeRepo = FakeUserRepository()

        application {
            install(ServerContentNegotiation) {
                json()
            }
            routing {
                userRoutes(fakeRepo)
            }
        }

        val client = createClient {
            install(ClientContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }

        // Act
        val response = client.get("/users")

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)

        val users = response.body<List<User>>()
        assertEquals(0, users.size)
    }

    @Test
    fun `POST users register - should return 200 when registration is successful`() = testApplication {
        // Arrange
        val fakeRepo = FakeUserRepository()

        application {
            install(ServerContentNegotiation) {
                json()
            }
            routing {
                userRoutes(fakeRepo)
            }
        }

        val client = createClient {
            install(ClientContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }

        val registerDto = UserRegisterDto(
            firstName = "Test",
            lastName = "User",
            userName = "testuser",
            email = "test@example.com",
            password = "password123"
        )

        // Act
        val response = client.post("/users/register") {
            contentType(ContentType.Application.Json)
            setBody(registerDto)
        }

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `POST users registerAdmin - should return 200 when admin registration is successful`() = testApplication {
        // Arrange
        val fakeRepo = FakeUserRepository()

        application {
            install(ServerContentNegotiation) {
                json()
            }
            routing {
                userRoutes(fakeRepo)
            }
        }

        val client = createClient {
            install(ClientContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }

        val registerDto = UserRegisterDto(
            firstName = "Admin",
            lastName = "User",
            userName = "adminuser",
            email = "admin@example.com",
            password = "password123"
        )

        // Act
        val response = client.post("/users/registerAdmin") {
            contentType(ContentType.Application.Json)
            setBody(registerDto)
        }

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `POST login - should return 200 with token when credentials are valid`() = testApplication {
        // Arrange
        val fakeRepo = FakeUserRepository()

        application {
            install(ServerContentNegotiation) {
                json()
            }
            routing {
                userRoutes(fakeRepo)
            }
        }

        val client = createClient {
            install(ClientContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }

        // First register a user
        val registerDto = UserRegisterDto(
            firstName = "Login",
            lastName = "Test",
            userName = "logintest",
            email = "login@example.com",
            password = "password123"
        )

        client.post("/users/register") {
            contentType(ContentType.Application.Json)
            setBody(registerDto)
        }

        // Now try to login
        val loginDto = userLoginDto(
            loginName = "logintest",
            password = "password123"
        )

        // Act
        val response = client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody(loginDto)
        }

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)

        val responseBody = response.body<Map<String, Any>>()
        assertTrue(responseBody.containsKey("token"))
        assertNotNull(responseBody["token"])
    }

    @Test
    fun `POST login - should return error when credentials are invalid`() = testApplication {
        // Arrange
        val fakeRepo = FakeUserRepository()

        application {
            install(ServerContentNegotiation) {
                json()
            }
            routing {
                userRoutes(fakeRepo)
            }
        }

        val client = createClient {
            install(ClientContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }

        val loginDto = userLoginDto(
            loginName = "nonexistent",
            password = "wrongpassword"
        )

        // Act
        val response = client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody(loginDto)
        }

        // Assert
        assertTrue(response.status.value >= 400)
    }
}
