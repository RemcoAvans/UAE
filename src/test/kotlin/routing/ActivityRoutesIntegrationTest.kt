package routing

import com.example.repository.ActivityTagRepository
import com.example.repository.TagRepository
import dtos.activity.ActivityFilterDto
import dtos.activity.CreateFoodActivityDto
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
import model.Activity
import org.junit.Test
import repository.ActivityRepository
import repository.ActivityVoteRepository
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ActivityRoutesIntegrationTest {

    // Fake repository voor testing
    class FakeActivityRepository(
        private val activities: MutableList<Activity> = mutableListOf()
    ) : ActivityRepository() {

        override suspend fun getAll(): List<Activity> = activities

        override suspend fun getById(id: Int): Activity? {
            return activities.find { it.id == id }
        }

        override suspend fun getByQuery(predicate: (Activity) -> Boolean): List<Activity> {
            return activities.filter(predicate)
        }

        override suspend fun create(entity: Activity): Activity {
            activities.add(entity)
            return entity
        }

        override suspend fun update(id: Int, entity: Activity): Activity? {
            val index = activities.indexOfFirst { it.id == id }
            if (index != -1) {
                activities[index] = entity
                return entity
            }
            return null
        }

        override suspend fun delete(id: Int): Boolean =
            activities.removeIf { it.id == id }

        fun addActivity(activity: Activity) {
            activities.add(activity)
        }

        fun clearActivities() {
            activities.clear()
        }
    }

    class FakeActivityVoteRepository : ActivityVoteRepository() {
        // Minimal implementation for testing
    }

    class FakeTagRepository : TagRepository() {
        // Minimal implementation for testing
    }

    class FakeActivityTagRepository : ActivityTagRepository() {
        // Minimal implementation for testing
    }

    @Test
    fun `GET activities - should return 200 with all activities`() = testApplication {
        // Arrange
        val fakeRepo = FakeActivityRepository()
        val fakeVoteRepo = FakeActivityVoteRepository()
        val fakeTagRepo = FakeTagRepository()
        val fakeActivityTagRepo = FakeActivityTagRepository()

        fakeRepo.addActivity(Activity(
            id = 1,
            title = "Pizza Night",
            description = "Enjoy authentic Italian pizza",
            type = "Food",
            price = 25.0,
            locationId = 1,
            capacity = 20,
            isFull = false,
            startDate = LocalDate.parse("2025-11-01"),
            endDate = LocalDate.parse("2025-11-01")
        ))
        fakeRepo.addActivity(Activity(
            id = 2,
            title = "Museum Tour",
            description = "Guided tour of the city museum",
            type = "Culture",
            price = 15.0,
            locationId = 2,
            capacity = 30,
            isFull = false,
            startDate = LocalDate.parse("2025-11-02"),
            endDate = LocalDate.parse("2025-11-02")
        ))

        application {
            install(ServerContentNegotiation) {
                json()
            }
            routing {
                activityRoutes(fakeRepo, fakeVoteRepo, fakeTagRepo, fakeActivityTagRepo)
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
        val response = client.get("/activities")

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)

        val activities = response.body<List<Activity>>()
        assertEquals(2, activities.size)
    }

    @Test
    fun `GET activities - should return 200 with empty list when no activities exist`() = testApplication {
        // Arrange
        val fakeRepo = FakeActivityRepository()
        val fakeVoteRepo = FakeActivityVoteRepository()
        val fakeTagRepo = FakeTagRepository()
        val fakeActivityTagRepo = FakeActivityTagRepository()

        application {
            install(ServerContentNegotiation) {
                json()
            }
            routing {
                activityRoutes(fakeRepo, fakeVoteRepo, fakeTagRepo, fakeActivityTagRepo)
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
        val response = client.get("/activities")

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)

        val activities = response.body<List<Activity>>()
        assertEquals(0, activities.size)
    }

    @Test
    fun `GET activities by id - should return 200 with activity when activity exists`() = testApplication {
        // Arrange
        val fakeRepo = FakeActivityRepository()
        val fakeVoteRepo = FakeActivityVoteRepository()
        val fakeTagRepo = FakeTagRepository()
        val fakeActivityTagRepo = FakeActivityTagRepository()

        fakeRepo.addActivity(Activity(
            id = 1,
            title = "Pizza Night",
            description = "Enjoy authentic Italian pizza",
            type = "Food",
            price = 25.0,
            locationId = 1,
            capacity = 20,
            isFull = false,
            startDate = LocalDate.parse("2025-11-01"),
            endDate = LocalDate.parse("2025-11-01")
        ))

        application {
            install(ServerContentNegotiation) {
                json()
            }
            routing {
                activityRoutes(fakeRepo, fakeVoteRepo, fakeTagRepo, fakeActivityTagRepo)
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
        val response = client.get("/activities/1")

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)

        val activity = response.body<Activity>()
        assertEquals(1, activity.id)
        assertEquals("Pizza Night", activity.title)
    }

    @Test
    fun `GET activities by id - should return 404 when activity does not exist`() = testApplication {
        // Arrange
        val fakeRepo = FakeActivityRepository()
        val fakeVoteRepo = FakeActivityVoteRepository()
        val fakeTagRepo = FakeTagRepository()
        val fakeActivityTagRepo = FakeActivityTagRepository()

        application {
            install(ServerContentNegotiation) {
                json()
            }
            routing {
                activityRoutes(fakeRepo, fakeVoteRepo, fakeTagRepo, fakeActivityTagRepo)
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
        val response = client.get("/activities/999")

        // Assert
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `GET activities by id - should return 400 when id is invalid`() = testApplication {
        // Arrange
        val fakeRepo = FakeActivityRepository()
        val fakeVoteRepo = FakeActivityVoteRepository()
        val fakeTagRepo = FakeTagRepository()
        val fakeActivityTagRepo = FakeActivityTagRepository()

        application {
            install(ServerContentNegotiation) {
                json()
            }
            routing {
                activityRoutes(fakeRepo, fakeVoteRepo, fakeTagRepo, fakeActivityTagRepo)
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
        val response = client.get("/activities/abc")

        // Assert
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `POST activities food - should return 200 when food activity is created successfully`() = testApplication {
        // Arrange
        val fakeRepo = FakeActivityRepository()
        val fakeVoteRepo = FakeActivityVoteRepository()
        val fakeTagRepo = FakeTagRepository()
        val fakeActivityTagRepo = FakeActivityTagRepository()

        application {
            install(ServerContentNegotiation) {
                json()
            }
            routing {
                activityRoutes(fakeRepo, fakeVoteRepo, fakeTagRepo, fakeActivityTagRepo)
            }
        }

        val client = createClient {
            install(ClientContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }

        val createFoodDto = CreateFoodActivityDto(
            title = "Pizza Night",
            description = "Enjoy authentic Italian pizza",
            type = "Food",
            price = 25.0,
            locationId = 1,
            capacity = 20,
            startDate = LocalDate.parse("2025-11-01"),
            endDate = LocalDate.parse("2025-11-01"),
            recurrencePattern = "",
            recurrenceDays = "",
            Cuisine = "Italian",
            PriceRange = "$$"
        )

        // Act
        val response = client.post("/activities/food") {
            contentType(ContentType.Application.Json)
            setBody(createFoodDto)
        }

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `POST activities filter - should return 200 with filtered activities`() = testApplication {
        // Arrange
        val fakeRepo = FakeActivityRepository()
        val fakeVoteRepo = FakeActivityVoteRepository()
        val fakeTagRepo = FakeTagRepository()
        val fakeActivityTagRepo = FakeActivityTagRepository()

        fakeRepo.addActivity(Activity(
            id = 1,
            title = "Pizza Night",
            description = "Enjoy authentic Italian pizza",
            type = "Food",
            price = 25.0,
            locationId = 1,
            capacity = 20,
            isFull = false,
            startDate = LocalDate.parse("2025-11-01"),
            endDate = LocalDate.parse("2025-11-01")
        ))
        fakeRepo.addActivity(Activity(
            id = 2,
            title = "Museum Tour",
            description = "Guided tour of the city museum",
            type = "Culture",
            price = 15.0,
            locationId = 2,
            capacity = 30,
            isFull = false,
            startDate = LocalDate.parse("2025-11-02"),
            endDate = LocalDate.parse("2025-11-02")
        ))

        application {
            install(ServerContentNegotiation) {
                json()
            }
            routing {
                activityRoutes(fakeRepo, fakeVoteRepo, fakeTagRepo, fakeActivityTagRepo)
            }
        }

        val client = createClient {
            install(ClientContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }

        val filterDto = ActivityFilterDto(
            categories = listOf("Food"),
            minPrice = 20,
            maxPrice = 30
        )

        // Act
        val response = client.post("/activities/filter") {
            contentType(ContentType.Application.Json)
            setBody(filterDto)
        }

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)

        val activities = response.body<List<Activity>>()
        assertTrue(activities.all { it.type == "Food" })
    }

    @Test
    fun `POST activities search - should return 200 with matching activities`() = testApplication {
        // Arrange
        val fakeRepo = FakeActivityRepository()
        val fakeVoteRepo = FakeActivityVoteRepository()
        val fakeTagRepo = FakeTagRepository()
        val fakeActivityTagRepo = FakeActivityTagRepository()

        fakeRepo.addActivity(Activity(
            id = 1,
            title = "Pizza Night",
            description = "Enjoy authentic Italian pizza",
            type = "Food",
            price = 25.0,
            locationId = 1,
            capacity = 20,
            isFull = false,
            startDate = LocalDate.parse("2025-11-01"),
            endDate = LocalDate.parse("2025-11-01")
        ))
        fakeRepo.addActivity(Activity(
            id = 2,
            title = "Sushi Workshop",
            description = "Learn to make sushi",
            type = "Food",
            price = 35.0,
            locationId = 1,
            capacity = 15,
            isFull = false,
            startDate = LocalDate.parse("2025-11-03"),
            endDate = LocalDate.parse("2025-11-03")
        ))

        application {
            install(ServerContentNegotiation) {
                json()
            }
            routing {
                activityRoutes(fakeRepo, fakeVoteRepo, fakeTagRepo, fakeActivityTagRepo)
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
        val response = client.post("/activities/search") {
            contentType(ContentType.Text.Plain)
            setBody("Pizza")
        }

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)

        val activities = response.body<List<Activity>>()
        assertTrue(activities.any { it.title.contains("Pizza") })
    }
}
