package routing

import com.example.model.ActivityTag
import com.example.model.Tag
import com.example.repository.ActivityTagRepository
import com.example.repository.TagRepository
import com.example.routing.tagRoutes
import com.example.usecase.tag.LinkTagInput
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation as ClientContentNegotiation
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation as ServerContentNegotiation
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TagRoutesIntegrationTest {

    // Fake repository voor testing
    class FakeTagRepository(
        private val tags: MutableList<Tag> = mutableListOf()
    ) : TagRepository() {

        override suspend fun getAll(): List<Tag> = tags

        override suspend fun getById(id: Int): Tag? {
            return tags.find { it.id == id }
        }

        override suspend fun getByQuery(predicate: (Tag) -> Boolean): List<Tag> {
            return tags.filter(predicate)
        }

        override suspend fun create(entity: Tag): Tag {
            tags.add(entity)
            return entity
        }

        override suspend fun update(id: Int, entity: Tag): Tag? {
            val index = tags.indexOfFirst { it.id == id }
            if (index != -1) {
                tags[index] = entity
                return entity
            }
            return null
        }

        override suspend fun delete(id: Int): Boolean =
            tags.removeIf { it.id == id }

        fun addTag(tag: Tag) {
            tags.add(tag)
        }

        fun clearTags() {
            tags.clear()
        }
    }

    class FakeActivityTagRepository(
        private val activityTags: MutableList<ActivityTag> = mutableListOf()
    ) : ActivityTagRepository() {

        override suspend fun getByQuery(predicate: (ActivityTag) -> Boolean): List<ActivityTag> {
            return activityTags.filter(predicate)
        }

        fun addActivityTag(activityTag: ActivityTag) {
            activityTags.add(activityTag)
        }

        fun clearActivityTags() {
            activityTags.clear()
        }
    }

    @Test
    fun `GET tags - should return 200 with all tags`() = testApplication {
        // Arrange
        val fakeTagRepo = FakeTagRepository()

        fakeTagRepo.addTag(Tag(id = 1, name = "Outdoor"))
        fakeTagRepo.addTag(Tag(id = 2, name = "Family"))
        fakeTagRepo.addTag(Tag(id = 3, name = "Evening"))

        application {
            install(ServerContentNegotiation) {
                json()
            }
            routing {
                tagRoutes(fakeTagRepo)
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
        val response = client.get("/tags")

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)

        val tags = response.body<List<Tag>>()
        assertEquals(3, tags.size)
    }

    @Test
    fun `GET tags - should return 200 with empty list when no tags exist`() = testApplication {
        // Arrange
        val fakeTagRepo = FakeTagRepository()

        application {
            install(ServerContentNegotiation) {
                json()
            }
            routing {
                tagRoutes(fakeTagRepo)
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
        val response = client.get("/tags")

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)

        val tags = response.body<List<Tag>>()
        assertEquals(0, tags.size)
    }

    @Test
    fun `GET tags by activityId - should return 200 with tags for activity`() = testApplication {
        // Arrange
        val fakeTagRepo = FakeTagRepository()
        val fakeActivityTagRepo = FakeActivityTagRepository()

        fakeTagRepo.addTag(Tag(id = 1, name = "Outdoor"))
        fakeTagRepo.addTag(Tag(id = 2, name = "Family"))

        fakeActivityTagRepo.addActivityTag(ActivityTag(id = 1, ActivityId = 1, TagId = 1))
        fakeActivityTagRepo.addActivityTag(ActivityTag(id = 2, ActivityId = 1, TagId = 2))

        application {
            install(ServerContentNegotiation) {
                json()
            }
            routing {
                tagRoutes(fakeTagRepo)
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
        val response = client.get("/tags/1")

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)

        val tags = response.body<List<Tag>>()
        assertEquals(2, tags.size)
    }

    @Test
    fun `GET tags by activityId - should return 400 when activityId is invalid`() = testApplication {
        // Arrange
        val fakeTagRepo = FakeTagRepository()

        application {
            install(ServerContentNegotiation) {
                json()
            }
            routing {
                tagRoutes(fakeTagRepo)
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
        val response = client.get("/tags/abc")

        // Assert
        assertEquals(HttpStatusCode.BadRequest, response.status)

        val errorResponse = response.body<Map<String, String>>()
        assertTrue(errorResponse.containsKey("error"))
        assertEquals("Ongeldige of geen activity ID", errorResponse["error"])
    }

    @Test
    fun `POST tags - should return 200 when tag is created successfully`() = testApplication {
        // Arrange
        val fakeTagRepo = FakeTagRepository()

        application {
            install(ServerContentNegotiation) {
                json()
            }
            routing {
                tagRoutes(fakeTagRepo)
            }
        }

        val client = createClient {
            install(ClientContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }

        val newTag = Tag(id = 1, name = "New Tag")

        // Act
        val response = client.post("/tags") {
            contentType(ContentType.Application.Json)
            setBody(newTag)
        }

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `PATCH tags - should return 200 when tag is updated successfully`() = testApplication {
        // Arrange
        val fakeTagRepo = FakeTagRepository()

        fakeTagRepo.addTag(Tag(id = 1, name = "Old Name"))

        application {
            install(ServerContentNegotiation) {
                json()
            }
            routing {
                tagRoutes(fakeTagRepo)
            }
        }

        val client = createClient {
            install(ClientContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }

        val updatedTag = Tag(id = 1, name = "Updated Name")

        // Act
        val response = client.patch("/tags") {
            contentType(ContentType.Application.Json)
            setBody(updatedTag)
        }

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `DELETE tags by id - should return 200 when tag is deleted successfully`() = testApplication {
        // Arrange
        val fakeTagRepo = FakeTagRepository()

        fakeTagRepo.addTag(Tag(id = 1, name = "To Delete"))

        application {
            install(ServerContentNegotiation) {
                json()
            }
            routing {
                tagRoutes(fakeTagRepo)
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
        val response = client.delete("/tags/1")

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `DELETE tags by id - should return 400 when id is invalid`() = testApplication {
        // Arrange
        val fakeTagRepo = FakeTagRepository()

        application {
            install(ServerContentNegotiation) {
                json()
            }
            routing {
                tagRoutes(fakeTagRepo)
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
        val response = client.delete("/tags/abc")

        // Assert
        assertEquals(HttpStatusCode.BadRequest, response.status)

        val errorResponse = response.body<Map<String, String>>()
        assertTrue(errorResponse.containsKey("error"))
        assertEquals("Ongeldige of geen tag ID", errorResponse["error"])
    }

    @Test
    fun `DELETE tags by id - should return 404 when tag does not exist`() = testApplication {
        // Arrange
        val fakeTagRepo = FakeTagRepository()

        application {
            install(ServerContentNegotiation) {
                json()
            }
            routing {
                tagRoutes(fakeTagRepo)
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
        val response = client.delete("/tags/999")

        // Assert
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `POST tags link - should return 200 when tag is linked to activity successfully`() = testApplication {
        // Arrange
        val fakeTagRepo = FakeTagRepository()

        application {
            install(ServerContentNegotiation) {
                json()
            }
            routing {
                tagRoutes(fakeTagRepo)
            }
        }

        val client = createClient {
            install(ClientContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }

        val linkInput = LinkTagInput(activityId = 1, tagId = 1)

        // Act
        val response = client.post("/tags/link") {
            contentType(ContentType.Application.Json)
            setBody(linkInput)
        }

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)
    }
}
