package routing

import com.example.model.AnalyticsData
import com.example.repository.IAnalyticsDataRepository
import com.example.routing.analyticsDataRoutes
import com.example.usecase.activity.GetActivityDetailsUseCaseTest
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
import repository.AnalyticsDataRepository
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AnalyticsDataRoutesIntegrationTest {

    // Fake repository voor testing
    class FakeAnalyticsDataRepository(
        private val analyticsData: MutableList<AnalyticsData> = mutableListOf()
    ) : repository.AnalyticsDataRepository(), IAnalyticsDataRepository {

        override suspend fun getAll(): List<AnalyticsData> = analyticsData

        override suspend fun getById(id: Int): AnalyticsData? {
            return analyticsData.find { it.id == id }
        }

        override suspend fun getByQuery(predicate: (AnalyticsData) -> Boolean): List<AnalyticsData> {
            return analyticsData.filter(predicate)
        }

        override suspend fun create(entity: AnalyticsData): AnalyticsData {
            analyticsData.add(entity)
            return entity
        }

        override suspend fun update(id: Int, entity: AnalyticsData): AnalyticsData? {
            val index = analyticsData.indexOfFirst { it.id == id }
            return if (index != -1) {
                analyticsData[index] = entity
                entity
            } else {
                null
            }
        }

        override suspend fun delete(id: Int): Boolean {
            return analyticsData.removeIf { it.id == id }
        }

        override suspend fun getByLocationId(locationId: Int): AnalyticsData? {
            return analyticsData.find { it.locationId == locationId }
        }

        fun addAnalyticsData(data: AnalyticsData) {
            analyticsData.add(data)
        }

        fun clearAnalyticsData() {
            analyticsData.clear()
        }
    }
    var activityRepo = GetActivityDetailsUseCaseTest.FakeActivityRepository(
    Activity(
    id = 1,
    title = "Boulderen",
    description = "Indoor klimmen",
    photoUrl = "photo.jpg",
    type = "Sport",
    price = 15.0,
    createdByUserId = 10,
    locationId = 5,
    isFeatured = true,
    capacity = 20,
    isFull = false,
    startDate = LocalDate.parse("2025-10-21"),
    endDate = LocalDate.parse("2025-10-22"),
    recurrencePattern = "",
    recurrenceDays = "",
    createdAt = LocalDate.parse("2025-10-01")
    )
    )

    @Test
    fun `GET analyticsData - should return 200 with all analytics data`() = testApplication {
        // Arrange
        val fakeRepo = FakeAnalyticsDataRepository()

        fakeRepo.addAnalyticsData(AnalyticsData(
            id = 1,
            locationId = 1,
            activityCount = 10,
            lastUpdated = LocalDate.parse("2025-10-20")
        ))
        fakeRepo.addAnalyticsData(AnalyticsData(
            id = 2,
            locationId = 2,
            activityCount = 15,
            lastUpdated = LocalDate.parse("2025-10-21")
        ))
        fakeRepo.addAnalyticsData(AnalyticsData(
            id = 3,
            locationId = 3,
            activityCount = 8,
            lastUpdated = LocalDate.parse("2025-10-22")
        ))

        application {
            install(ServerContentNegotiation) {
                json()
            }
            routing {
                analyticsDataRoutes(fakeRepo, activityRepo)
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
        val response = client.get("/analyticsData")

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)

        val data = response.body<List<AnalyticsData>>()
        assertEquals(3, data.size)
    }

    @Test
    fun `GET analyticsData by activityId - should return 400 when activityId is invalid`() = testApplication {
        // Arrange
        val fakeRepo = FakeAnalyticsDataRepository()

        application {
            install(ServerContentNegotiation) {
                json()
            }
            routing {
                analyticsDataRoutes(fakeRepo, activityRepo)
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
        val response = client.get("/analyticsData/abc")

        // Assert
        assertEquals(HttpStatusCode.BadRequest, response.status)

        val errorResponse = response.body<Map<String, String>>()
        assertTrue(errorResponse.containsKey("error"))
        assertEquals("Ongeldige of geen activity ID", errorResponse["error"])
    }

    @Test
    fun `GET analyticsData by activityId - should return 404 when no data exists for activity`() = testApplication {
        // Arrange
        val fakeRepo = FakeAnalyticsDataRepository()

        application {
            install(ServerContentNegotiation) {
                json()
            }
            routing {
                analyticsDataRoutes(fakeRepo, activityRepo)
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
        val response = client.get("/analyticsData/999")

        // Assert
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `GET analyticsData - should correctly filter data by location`() = testApplication {
        // Arrange
        val fakeRepo = FakeAnalyticsDataRepository()

        fakeRepo.addAnalyticsData(AnalyticsData(
            id = 1,
            locationId = 1,
            activityCount = 10,
            lastUpdated = LocalDate.parse("2025-10-20")
        ))
        fakeRepo.addAnalyticsData(AnalyticsData(
            id = 2,
            locationId = 1,
            activityCount = 5,
            lastUpdated = LocalDate.parse("2025-10-21")
        ))
        fakeRepo.addAnalyticsData(AnalyticsData(
            id = 3,
            locationId = 2,
            activityCount = 8,
            lastUpdated = LocalDate.parse("2025-10-22")
        ))

        application {
            install(ServerContentNegotiation) {
                json()
            }
            routing {
                analyticsDataRoutes(fakeRepo, activityRepo)
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
        val response = client.get("/analyticsData")

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)

        val data = response.body<List<AnalyticsData>>()
        val location1Data = data.filter { it.locationId == 1 }
        assertEquals(2, location1Data.size)
    }
}
