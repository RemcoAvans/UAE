package com.example.usecase.activity

import com.example.repository.IActivityRepository
import dtos.activity.ActivityFilterDto
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import model.Activity
import org.junit.Assert.*
import org.junit.Test

class FilterActivitiesUseCaseTest {

    // Fake repository for testing
    class FakeActivityRepository(private val activities: List<Activity>) : IActivityRepository {
        override suspend fun getAll(): List<Activity> = activities
        override suspend fun getById(id: Int): Activity? = activities.find { it.id == id }
        override suspend fun getByQuery(predicate: (Activity) -> Boolean): List<Activity> =
            activities.filter(predicate)
        override suspend fun create(entity: Activity): Activity = entity
        override suspend fun update(id: Int, entity: Activity): Activity? = entity
        override suspend fun delete(id: Int): Boolean = true
        override fun createSport(sportActivity: com.example.model.SportActivity) = sportActivity
        override fun createFood(foodActivity: com.example.model.FoodActivity) = foodActivity
        override fun createCulture(cultureActivity: com.example.model.CultureActivity) = cultureActivity
        override fun setFeatured(id: Int, featured: Boolean): Activity? = null
        override fun getFeaturedActivities(): List<Activity> = activities.filter { it.isFeatured }
        override fun updatePhotoUrl(id: Int, photoUrl: String) {}
    }

    private val testActivities = listOf(
        Activity(
            id = 1,
            title = "Pizza Night",
            description = "Italian pizza",
            type = "Food",
            price = 25.0,
            locationId = 1,
            capacity = 20,
            isFull = false,
            isFeatured = true,
            startDate = LocalDate.parse("2025-11-01"),
            endDate = LocalDate.parse("2025-11-01")
        ),
        Activity(
            id = 2,
            title = "Museum Tour",
            description = "City museum",
            type = "Culture",
            price = 15.0,
            locationId = 2,
            capacity = 30,
            isFull = false,
            isFeatured = false,
            startDate = LocalDate.parse("2025-11-02"),
            endDate = LocalDate.parse("2025-11-02")
        ),
        Activity(
            id = 3,
            title = "Soccer Game",
            description = "Play soccer",
            type = "Sport",
            price = 10.0,
            locationId = 1,
            capacity = 22,
            isFull = false,
            isFeatured = false,
            startDate = LocalDate.parse("2025-11-03"),
            endDate = LocalDate.parse("2025-11-03")
        ),
        Activity(
            id = 4,
            title = "Fine Dining",
            description = "Fancy restaurant",
            type = "Food",
            price = 50.0,
            locationId = 2,
            capacity = 15,
            isFull = false,
            isFeatured = true,
            startDate = LocalDate.parse("2025-11-04"),
            endDate = LocalDate.parse("2025-11-04")
        )
    )

    @Test
    fun `should filter activities by price range`() = runBlocking {
        // Arrange
        val useCase = FilterActivitiesUseCase(FakeActivityRepository(testActivities))
        val filter = ActivityFilterDto(minPrice = 20, maxPrice = 30)

        // Act
        val result = useCase.execute(filter)

        // Assert
        assertTrue(result.success)
        assertEquals(1, result.result?.size)
        assertEquals("Pizza Night", result.result?.first()?.title)
    }

    @Test
    fun `should filter activities by location`() = runBlocking {
        // Arrange
        val useCase = FilterActivitiesUseCase(FakeActivityRepository(testActivities))
        val filter = ActivityFilterDto(locationId = 1)

        // Act
        val result = useCase.execute(filter)

        // Assert
        assertTrue(result.success)
        assertEquals(2, result.result?.size)
        assertTrue(result.result?.all { it.locationId == 1 } ?: false)
    }

    @Test
    fun `should filter activities by category`() = runBlocking {
        // Arrange
        val useCase = FilterActivitiesUseCase(FakeActivityRepository(testActivities))
        val filter = ActivityFilterDto(categories = listOf("Food"))

        // Act
        val result = useCase.execute(filter)

        // Assert
        assertTrue(result.success)
        assertEquals(2, result.result?.size)
        assertTrue(result.result?.all { it.type == "Food" } ?: false)
    }

    @Test
    fun `should filter activities by multiple criteria`() = runBlocking {
        // Arrange
        val useCase = FilterActivitiesUseCase(FakeActivityRepository(testActivities))
        val filter = ActivityFilterDto(
            categories = listOf("Food"),
            minPrice = 20,
            maxPrice = 40,
            locationId = 1
        )

        // Act
        val result = useCase.execute(filter)

        // Assert
        assertTrue(result.success)
        assertEquals(1, result.result?.size)
        assertEquals("Pizza Night", result.result?.first()?.title)
    }

    @Test
    fun `should sort featured activities first`() = runBlocking {
        // Arrange
        val useCase = FilterActivitiesUseCase(FakeActivityRepository(testActivities))
        val filter = ActivityFilterDto() // No filters

        // Act
        val result = useCase.execute(filter)

        // Assert
        assertTrue(result.success)
        assertEquals(4, result.result?.size)
        // First two should be featured
        assertTrue(result.result?.get(0)?.isFeatured ?: false)
        assertTrue(result.result?.get(1)?.isFeatured ?: false)
    }

    @Test
    fun `should return empty list when no activities match filter`() = runBlocking {
        // Arrange
        val useCase = FilterActivitiesUseCase(FakeActivityRepository(testActivities))
        val filter = ActivityFilterDto(minPrice = 100, maxPrice = 200)

        // Act
        val result = useCase.execute(filter)

        // Assert
        assertTrue(result.success)
        assertEquals(0, result.result?.size)
    }

    @Test
    fun `should return not found when repository is empty`() = runBlocking {
        // Arrange
        val useCase = FilterActivitiesUseCase(FakeActivityRepository(emptyList()))
        val filter = ActivityFilterDto()

        // Act
        val result = useCase.execute(filter)

        // Assert
        assertFalse(result.success)
        assertEquals(HttpStatusCode.NotFound, result.statusCode)
        assertEquals("Geen activiteiten gevonden.", result.message)
    }

    @Test
    fun `should filter by max price only`() = runBlocking {
        // Arrange
        val useCase = FilterActivitiesUseCase(FakeActivityRepository(testActivities))
        val filter = ActivityFilterDto(maxPrice = 20)

        // Act
        val result = useCase.execute(filter)

        // Assert
        assertTrue(result.success)
        assertEquals(2, result.result?.size)
        assertTrue(result.result?.all { it.price <= 20 } ?: false)
    }

    @Test
    fun `should filter by min price only`() = runBlocking {
        // Arrange
        val useCase = FilterActivitiesUseCase(FakeActivityRepository(testActivities))
        val filter = ActivityFilterDto(minPrice = 20)

        // Act
        val result = useCase.execute(filter)

        // Assert
        assertTrue(result.success)
        assertEquals(2, result.result?.size)
        assertTrue(result.result?.all { it.price >= 20 } ?: false)
    }
}
