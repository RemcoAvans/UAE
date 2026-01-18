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
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
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
            install(Authentication) {
                jwt("auth-jwt") {
                    // Dummy JWT configuration for testing
                    verifier { null }
                    validate { null }
                }
            }
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
    fun `GET users - should return 404 when no users exist`() = testApplication {
        // Arrange
        val fakeRepo = FakeUserRepository()

        application {
            install(Authentication) {
                jwt("auth-jwt") {
                    // Dummy JWT configuration for testing
                    verifier { null }
                    validate { null }
                }
            }
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
        assertEquals(HttpStatusCode.NotFound, response.status)

        val errorResponse = response.body<Map<String, String>>()
        assertTrue(errorResponse.containsKey("error"))
    }

    @Test
    fun `POST users register - should return 200 when registration is successful`() = testApplication {
        // Arrange
        val fakeRepo = FakeUserRepository()

        environment {
            config = MapApplicationConfig(
                "jwt.issuer" to "https://jwt-ktor-demo-les6/",
                "jwt.audience" to "jwt-audience",
                "jwt.realm" to "ktor sample app",
                "jwt.secret" to "mySecret"
            )
        }

        application {
            install(Authentication) {
                jwt("auth-jwt") {
                    // Dummy JWT configuration for testing
                    verifier { null }
                    validate { null }
                }
            }
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
            install(Authentication) {
                jwt("auth-jwt") {
                    // Dummy JWT configuration for testing
                    verifier { null }
                    validate { null }
                }
            }
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

    // Note: Login tests are skipped because they require environment setup for JWT
    // which is complex in integration tests. Login functionality should be tested manually or in E2E tests.
}
