package com.example.usecase.activity

import com.example.core.ObjectResult
import com.example.dtos.activity.ActivityDetailDto
import com.example.model.ActivityTag
import com.example.model.ActivityVote
import com.example.model.Tag
import com.example.repository.ActivityTagRepository
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import model.Activity
import org.junit.Assert.*
import org.junit.Test
import repository.ActivityRepository
import repository.ActivityVoteRepository
import repository.CrudRepository

class GetActivityDetailsUseCaseTest {

    // ---------- Fake Repositories ----------
    class FakeActivityRepository(val activity: Activity?) : ActivityRepository() {
        override suspend fun getById(id: Int): Activity? = activity
    }

    class FakeVoteRepository(val votes: List<ActivityVote>) : ActivityVoteRepository() {
        override suspend fun getByQuery(filter: (ActivityVote) -> Boolean): List<ActivityVote> =
            votes.filter(filter)
    }

    class FakeActivityTagRepository(val tags: List<ActivityTag>) : ActivityTagRepository() {
        override suspend fun getByQuery(filter: (ActivityTag) -> Boolean): List<ActivityTag> =
            tags.filter(filter)
    }

    class FakeTagRepository(val tagMap: Map<Int, Tag>) : CrudRepository<Tag> {
        override suspend fun getAll(): List<Tag> = tagMap.values.toList()
        override suspend fun getById(id: Int): Tag? = tagMap[id]
        override suspend fun getByQuery(predicate: (Tag) -> Boolean): List<Tag> = tagMap.values.filter(predicate)
        override suspend fun create(entity: Tag): Tag = entity
        override suspend fun update(id: Int, entity: Tag): Tag? = entity
        override suspend fun delete(id: Int): Boolean = true
    }

    // ---------- TESTS ----------
    @Test
    fun `should return ActivityDetailDto with correct rating and tags`() = runBlocking {
        // Arrange
        val activity = Activity(
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

        val votes = listOf(
            ActivityVote(activityId = 1, id = 1, userId = 1, positive = true),
            ActivityVote(activityId = 1, id = 2, userId = 1, positive = true),
            ActivityVote(activityId = 1, id = 3, userId = 1, positive = false)
        )

        val activityTags = listOf(
            ActivityTag(id = 1, ActivityId = 1, TagId = 1),
            ActivityTag(id = 2, ActivityId = 1, TagId = 2)
        )

        val tags = mapOf(
            1 to Tag(id = 1, name = "Avontuur"),
            2 to Tag(id = 2, name = "Sportief")
        )

        val useCase = GetActivityDetailsUseCase(
            activityRepo = FakeActivityRepository(activity),
            voteRepo = FakeVoteRepository(votes),
            activityTagRepository = FakeActivityTagRepository(activityTags),
            tagRepo = FakeTagRepository(tags)
        )

        // Act
        val result = useCase.execute(1)

        // Assert
        assertTrue(result.success)
        val dto = result.result!!
        assertEquals(1, dto.rating) // (2 positieve - 1 negatieve)
        assertEquals(listOf("Avontuur", "Sportief"), dto.tags)
    }

    @Test
    fun `should return not found when activity not exists`() = runBlocking {
        // Arrange
        val useCase = GetActivityDetailsUseCase(
            activityRepo = FakeActivityRepository(null),
            voteRepo = FakeVoteRepository(emptyList()),
            activityTagRepository = FakeActivityTagRepository(emptyList()),
            tagRepo = FakeTagRepository(emptyMap())
        )

        // Act
        val result = useCase.execute(99)

        // Assert
        assertEquals(HttpStatusCode.NotFound, result.statusCode )
        assertEquals("Activiteit niet gevonden.", result.message)
    }
}
