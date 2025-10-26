package usecase.vote

import com.example.core.ObjectResult
import com.example.model.ActivityVote
import com.example.repository.IActivityVoteRepository
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class GetVotesByActivityUseCaseTest {

    // Fake repository for testing
    class FakeActivityVoteRepository(private val votes: List<ActivityVote>) : IActivityVoteRepository {
        override suspend fun getAll(): List<ActivityVote> = votes
        override suspend fun getById(id: Int): ActivityVote? = votes.find { it.id == id }
        override suspend fun getByQuery(predicate: (ActivityVote) -> Boolean): List<ActivityVote> =
            votes.filter(predicate)
        override suspend fun create(entity: ActivityVote): ActivityVote = entity
        override suspend fun update(entity: ActivityVote): Boolean = true
        override suspend fun delete(id: Int): Boolean = true
    }

    private val testVotes = listOf(
        ActivityVote(id = 1, activityId = 1, userId = 10, positive = true),
        ActivityVote(id = 2, activityId = 1, userId = 11, positive = true),
        ActivityVote(id = 3, activityId = 1, userId = 12, positive = false),
        ActivityVote(id = 4, activityId = 2, userId = 13, positive = true),
        ActivityVote(id = 5, activityId = 2, userId = 14, positive = false)
    )

    @Test
    fun `should return votes for activity with id 1`() = runBlocking {
        // Arrange
        val useCase = GetVotesByActivityUseCase(FakeActivityVoteRepository(testVotes))

        // Act
        val result = useCase.execute(1)

        // Assert
        assertTrue(result.success)
        assertEquals(3, result.result?.size)
        assertTrue(result.result?.all { it.activityId == 1 } ?: false)
    }

    @Test
    fun `should return votes for activity with id 2`() = runBlocking {
        // Arrange
        val useCase = GetVotesByActivityUseCase(FakeActivityVoteRepository(testVotes))

        // Act
        val result = useCase.execute(2)

        // Assert
        assertTrue(result.success)
        assertEquals(2, result.result?.size)
        assertTrue(result.result?.all { it.activityId == 2 } ?: false)
    }

    @Test
    fun `should return not found when activity has no votes`() = runBlocking {
        // Arrange
        val useCase = GetVotesByActivityUseCase(FakeActivityVoteRepository(testVotes))

        // Act
        val result = useCase.execute(999)

        // Assert
        assertFalse(result.success)
        assertEquals(HttpStatusCode.NotFound, result.statusCode)
        assertEquals("deze activiteit heeft 0 stemmen", result.message)
    }

    @Test
    fun `should return fail when activity id is null`() = runBlocking {
        // Arrange
        val useCase = GetVotesByActivityUseCase(FakeActivityVoteRepository(testVotes))

        // Act
        val result = useCase.execute(null)

        // Assert
        assertFalse(result.success)
        assertEquals(HttpStatusCode.BadRequest, result.statusCode)
        assertEquals("activityId is ongeldig", result.message)
    }

    @Test
    fun `should return fail when activity id is zero`() = runBlocking {
        // Arrange
        val useCase = GetVotesByActivityUseCase(FakeActivityVoteRepository(testVotes))

        // Act
        val result = useCase.execute(0)

        // Assert
        assertFalse(result.success)
        assertEquals(HttpStatusCode.BadRequest, result.statusCode)
        assertEquals("activityId is ongeldig", result.message)
    }

    @Test
    fun `should return fail when activity id is negative`() = runBlocking {
        // Arrange
        val useCase = GetVotesByActivityUseCase(FakeActivityVoteRepository(testVotes))

        // Act
        val result = useCase.execute(-1)

        // Assert
        assertFalse(result.success)
        assertEquals(HttpStatusCode.BadRequest, result.statusCode)
        assertEquals("activityId is ongeldig", result.message)
    }

    @Test
    fun `should return not found when repository is empty`() = runBlocking {
        // Arrange
        val useCase = GetVotesByActivityUseCase(FakeActivityVoteRepository(emptyList()))

        // Act
        val result = useCase.execute(1)

        // Assert
        assertFalse(result.success)
        assertEquals(HttpStatusCode.NotFound, result.statusCode)
        assertEquals("deze activiteit heeft 0 stemmen", result.message)
    }

    @Test
    fun `should correctly filter positive and negative votes`() = runBlocking {
        // Arrange
        val useCase = GetVotesByActivityUseCase(FakeActivityVoteRepository(testVotes))

        // Act
        val result = useCase.execute(1)

        // Assert
        assertTrue(result.success)
        val votes = result.result!!
        val positiveVotes = votes.filter { it.positive }
        val negativeVotes = votes.filter { !it.positive }

        assertEquals(2, positiveVotes.size)
        assertEquals(1, negativeVotes.size)
    }
}
