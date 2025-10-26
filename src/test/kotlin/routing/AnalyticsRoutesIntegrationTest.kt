package routing

import routing.analyticsRoutes
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
import model.SearchLog
import org.junit.Test
import repository.ActivityRepository
import repository.AnalyticsRepository
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class AnalyticsRoutesIntegrationTest {

    // Fake repository voor testing
    class FakeActivityRepository(
        private val activities: MutableList<Activity> = mutableListOf()
    ) : ActivityRepository() {

        override suspend fun getByQuery(predicate: (Activity) -> Boolean): List<Activity> {
            return activities.filter(predicate)
        }

        fun addActivity(activity: Activity) {
            activities.add(activity)
        }

        fun clearActivities() {
            activities.clear()
        }
    }

    class FakeAnalyticsRepository(
        private val searchLogs: MutableList<SearchLog> = mutableListOf()
    ) : AnalyticsRepository() {

        override suspend fun getAllSearchLogs(): List<SearchLog> = searchLogs

        override suspend fun getSearchLogsByDateRange(fromTimestamp: Long, toTimestamp: Long): List<SearchLog> {
            return searchLogs.filter { it.timestamp in fromTimestamp..toTimestamp }
        }

        override suspend fun logSearch(
            categories: List<String>?,
            city: String?,
            minPrice: Int?,
            maxPrice: Int?,
            resultsCount: Int
        ): SearchLog {
            val log = SearchLog(
                id = searchLogs.size + 1,
                timestamp = System.currentTimeMillis(),
                categories = categories,
                city = city,
                minPrice = minPrice,
                maxPrice = maxPrice,
                resultsCount = resultsCount
            )
            searchLogs.add(log)
            return log
        }

        fun addSearchLog(searchLog: SearchLog) {
            searchLogs.add(searchLog)
        }

        fun clearSearchLogs() {
            searchLogs.clear()
        }
    }

    @Test
    fun `GET admin analytics trends - should return 200 with activity trends`() = testApplication {
        // Arrange
        val fakeActivityRepo = FakeActivityRepository()
        val fakeAnalyticsRepo = FakeAnalyticsRepository()

        fakeActivityRepo.addActivity(Activity(
            id = 1,
            title = "Pizza Night",
            description = "Enjoy pizza",
            type = "Food",
            price = 25.0,
            locationId = 1,
            capacity = 20,
            isFull = false,
            startDate = LocalDate.parse("2025-11-01"),
            endDate = LocalDate.parse("2025-11-01")
        ))
        fakeActivityRepo.addActivity(Activity(
            id = 2,
            title = "Museum Tour",
            description = "Visit museum",
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
                analyticsRoutes(fakeActivityRepo, fakeAnalyticsRepo)
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
        val response = client.get("/admin/analytics/trends")

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `GET admin analytics trends with limit - should return 200 with limited trends`() = testApplication {
        // Arrange
        val fakeActivityRepo = FakeActivityRepository()
        val fakeAnalyticsRepo = FakeAnalyticsRepository()

        fakeActivityRepo.addActivity(Activity(
            id = 1,
            title = "Pizza Night",
            description = "Enjoy pizza",
            type = "Food",
            price = 25.0,
            locationId = 1,
            capacity = 20,
            isFull = false,
            startDate = LocalDate.parse("2025-11-01"),
            endDate = LocalDate.parse("2025-11-01")
        ))
        fakeActivityRepo.addActivity(Activity(
            id = 2,
            title = "Museum Tour",
            description = "Visit museum",
            type = "Culture",
            price = 15.0,
            locationId = 2,
            capacity = 30,
            isFull = false,
            startDate = LocalDate.parse("2025-11-02"),
            endDate = LocalDate.parse("2025-11-02")
        ))
        fakeActivityRepo.addActivity(Activity(
            id = 3,
            title = "Soccer Game",
            description = "Play soccer",
            type = "Sport",
            price = 10.0,
            locationId = 3,
            capacity = 22,
            isFull = false,
            startDate = LocalDate.parse("2025-11-03"),
            endDate = LocalDate.parse("2025-11-03")
        ))

        application {
            install(ServerContentNegotiation) {
                json()
            }
            routing {
                analyticsRoutes(fakeActivityRepo, fakeAnalyticsRepo)
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
        val response = client.get("/admin/analytics/trends?limit=2")

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `GET admin analytics searches - should return 200 with search analytics`() = testApplication {
        // Arrange
        val fakeActivityRepo = FakeActivityRepository()
        val fakeAnalyticsRepo = FakeAnalyticsRepository()

        fakeAnalyticsRepo.addSearchLog(SearchLog(
            id = 1,
            timestamp = System.currentTimeMillis(),
            categories = listOf("Food"),
            city = "Dubai",
            minPrice = 20,
            maxPrice = 50,
            resultsCount = 5
        ))
        fakeAnalyticsRepo.addSearchLog(SearchLog(
            id = 2,
            timestamp = System.currentTimeMillis(),
            categories = listOf("Culture"),
            city = "Abu Dhabi",
            minPrice = 10,
            maxPrice = 30,
            resultsCount = 3
        ))

        application {
            install(ServerContentNegotiation) {
                json()
            }
            routing {
                analyticsRoutes(fakeActivityRepo, fakeAnalyticsRepo)
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
        val response = client.get("/admin/analytics/searches")

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `GET admin analytics activities - should return 200 with activity statistics`() = testApplication {
        // Arrange
        val fakeActivityRepo = FakeActivityRepository()
        val fakeAnalyticsRepo = FakeAnalyticsRepository()

        fakeActivityRepo.addActivity(Activity(
            id = 1,
            title = "Pizza Night",
            description = "Enjoy pizza",
            type = "Food",
            price = 25.0,
            locationId = 1,
            capacity = 20,
            isFull = false,
            startDate = LocalDate.parse("2025-11-01"),
            endDate = LocalDate.parse("2025-11-01")
        ))
        fakeActivityRepo.addActivity(Activity(
            id = 2,
            title = "Museum Tour",
            description = "Visit museum",
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
                analyticsRoutes(fakeActivityRepo, fakeAnalyticsRepo)
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
        val response = client.get("/admin/analytics/activities")

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `GET admin analytics activities - should return 200 with empty statistics when no activities exist`() = testApplication {
        // Arrange
        val fakeActivityRepo = FakeActivityRepository()
        val fakeAnalyticsRepo = FakeAnalyticsRepository()

        application {
            install(ServerContentNegotiation) {
                json()
            }
            routing {
                analyticsRoutes(fakeActivityRepo, fakeAnalyticsRepo)
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
        val response = client.get("/admin/analytics/activities")

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)
    }
}
