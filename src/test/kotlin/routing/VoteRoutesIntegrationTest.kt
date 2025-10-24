package routing

import com.example.baseRouter.BaseRouter.handle
import com.example.model.ActivityVote
import com.example.routing.voteRoutes
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
import org.junit.Test
import repository.ActivityVoteRepository
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class VoteRoutesIntegrationTest {

    // Fake repository voor testing
    class FakeActivityVoteRepository(
        private val votes: MutableList<ActivityVote> = mutableListOf()
    ) : ActivityVoteRepository() {

        override suspend fun getByQuery(predicate: (ActivityVote) -> Boolean): List<ActivityVote> {
            return votes.filter(predicate)
        }

        fun addVote(vote: ActivityVote) {
            votes.add(vote)
        }

        fun clearVotes() {
            votes.clear()
        }
    }

    @Test
    fun `GET votes by activityId - should return 200 with votes when votes exist`() = testApplication {
        // Arrange
        val fakeRepo = FakeActivityVoteRepository()

        // Add test votes voor activity 1
        fakeRepo.addVote(ActivityVote(
            id = 1,
            activityId = 1,
            userId = 100,
            createAt = LocalDate.parse("2025-10-20"),
            positive = true
        ))
        fakeRepo.addVote(ActivityVote(
            id = 2,
            activityId = 1,
            userId = 101,
            createAt = LocalDate.parse("2025-10-21"),
            positive = false
        ))
        fakeRepo.addVote(ActivityVote(
            id = 3,
            activityId = 1,
            userId = 102,
            createAt = LocalDate.parse("2025-10-22"),
            positive = true
        ))

        // Configureer routing
        application {
            install(ServerContentNegotiation) {
                json()
            }
            routing {
                voteRoutes(fakeRepo)
            }
        }

        // Configureer client met JSON support
        val client = createClient {
            install(ClientContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }

        // Act
        val response = client.get("/votes/1")

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)

        val votes = response.body<List<ActivityVote>>()
        assertEquals(3, votes.size)
        assertEquals(1, votes[0].activityId)
        assertEquals(1, votes[1].activityId)
        assertEquals(1, votes[2].activityId)
        assertTrue(votes[0].positive)
    }

    @Test
    fun `GET votes by activityId - should return 404 when no votes exist for activity`() = testApplication {
        // Arrange
        val fakeRepo = FakeActivityVoteRepository()

        application {
            install(ServerContentNegotiation) {
                json()
            }
            routing {
                voteRoutes(fakeRepo)
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
        val response = client.get("/votes/999")

        // Assert
        assertEquals(HttpStatusCode.NotFound, response.status)

        val errorResponse = response.body<Map<String, String>>()
        assertTrue(errorResponse.containsKey("error"))
        assertEquals("deze activiteit heeft 0 stemmen", errorResponse["error"])
    }

    @Test
    fun `GET votes by activityId - should return 400 when activityId is invalid (null)`() = testApplication {
        // Arrange
        val fakeRepo = FakeActivityVoteRepository()

        application {
            install(ServerContentNegotiation) {
                json()
            }
            routing {
                voteRoutes(fakeRepo)
            }
        }

        val client = createClient {
            install(ClientContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }

        // Act - send invalid activityId
        val response = client.get("/votes/abc")

        // Assert
        assertEquals(HttpStatusCode.BadRequest, response.status)

        val errorResponse = response.body<Map<String, String>>()
        assertTrue(errorResponse.containsKey("error"))
        assertEquals("activityId is ongeldig", errorResponse["error"])
    }

    @Test
    fun `GET votes by activityId - should return 400 when activityId is zero`() = testApplication {
        // Arrange
        val fakeRepo = FakeActivityVoteRepository()

        application {
            install(ServerContentNegotiation) {
                json()
            }
            routing {
                voteRoutes(fakeRepo)
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
        val response = client.get("/votes/0")

        // Assert
        assertEquals(HttpStatusCode.BadRequest, response.status)

        val errorResponse = response.body<Map<String, String>>()
        assertTrue(errorResponse.containsKey("error"))
        assertEquals("activityId is ongeldig", errorResponse["error"])
    }

    @Test
    fun `GET votes by activityId - should return 400 when activityId is negative`() = testApplication {
        // Arrange
        val fakeRepo = FakeActivityVoteRepository()

        application {
            install(ServerContentNegotiation) {
                json()
            }
            routing {
                voteRoutes(fakeRepo)
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
        val response = client.get("/votes/-5")

        // Assert
        assertEquals(HttpStatusCode.BadRequest, response.status)

        val errorResponse = response.body<Map<String, String>>()
        assertTrue(errorResponse.containsKey("error"))
        assertEquals("activityId is ongeldig", errorResponse["error"])
    }

    @Test
    fun `GET votes by activityId - should only return votes for specific activity`() = testApplication {
        // Arrange
        val fakeRepo = FakeActivityVoteRepository()

        // Add votes voor verschillende activities
        fakeRepo.addVote(ActivityVote(id = 1, activityId = 1, userId = 100, positive = true))
        fakeRepo.addVote(ActivityVote(id = 2, activityId = 1, userId = 101, positive = false))
        fakeRepo.addVote(ActivityVote(id = 3, activityId = 2, userId = 102, positive = true))
        fakeRepo.addVote(ActivityVote(id = 4, activityId = 3, userId = 103, positive = false))

        application {
            install(ServerContentNegotiation) {
                json()
            }
            routing {
                voteRoutes(fakeRepo)
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
        val response = client.get("/votes/1")

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)

        val votes = response.body<List<ActivityVote>>()
        assertEquals(2, votes.size)

        // Verify all votes are for activity 1
        votes.forEach { vote ->
            assertEquals(1, vote.activityId)
        }
    }
}
