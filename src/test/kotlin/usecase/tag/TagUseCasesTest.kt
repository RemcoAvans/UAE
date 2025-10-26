package com.example.usecase.tag

import com.example.model.Tag
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import repository.CrudRepository

class TagUseCasesTest {

    // Fake repository for testing
    class FakeTagRepository(
        private val tags: MutableList<Tag> = mutableListOf()
    ) : CrudRepository<Tag> {
        override suspend fun getAll(): List<Tag> = tags
        override suspend fun getById(id: Int): Tag? = tags.find { it.id == id }
        override suspend fun getByQuery(predicate: (Tag) -> Boolean): List<Tag> = tags.filter(predicate)
        override suspend fun create(entity: Tag): Tag {
            tags.add(entity)
            return entity
        }
        override suspend fun update(id: Int, entity: Tag): Tag? {
            val index = tags.indexOfFirst { it.id == id }
            return if (index != -1) {
                tags[index] = entity
                entity
            } else null
        }
        override suspend fun delete(id: Int): Boolean = tags.removeIf { it.id == id }
    }

    // Tests for GetAllTagsUseCase
    @Test
    fun `GetAllTagsUseCase - should return all tags when tags exist`() = runBlocking {
        // Arrange
        val tags = mutableListOf(
            Tag(id = 1, name = "Outdoor"),
            Tag(id = 2, name = "Family"),
            Tag(id = 3, name = "Evening")
        )
        val repository = FakeTagRepository(tags)
        val useCase = GetAllTagsUseCase(repository)

        // Act
        val result = useCase.execute()

        // Assert
        assertTrue(result.success)
        assertEquals(3, result.result?.size)
    }

    @Test
    fun `GetAllTagsUseCase - should return not found when no tags exist`() = runBlocking {
        // Arrange
        val repository = FakeTagRepository()
        val useCase = GetAllTagsUseCase(repository)

        // Act
        val result = useCase.execute()

        // Assert
        assertFalse(result.success)
        assertEquals(HttpStatusCode.NotFound, result.statusCode)
        assertEquals("Geen tags gevonden", result.message)
    }

    // Tests for CreateTagUseCase
    @Test
    fun `CreateTagUseCase - should create tag successfully`() = runBlocking {
        // Arrange
        val repository = FakeTagRepository()
        val useCase = CreateTagUseCase(repository)
        val newTag = Tag(id = 1, name = "Adventure")

        // Act
        val result = useCase.execute(newTag)

        // Assert
        assertTrue(result.success)
        assertEquals("Adventure", result.result?.name)
        assertEquals(1, repository.getAll().size)
    }

    @Test
    fun `CreateTagUseCase - should fail when tag name already exists`() = runBlocking {
        // Arrange
        val tags = mutableListOf(Tag(id = 1, name = "Adventure"))
        val repository = FakeTagRepository(tags)
        val useCase = CreateTagUseCase(repository)
        val newTag = Tag(id = 2, name = "Adventure")

        // Act
        val result = useCase.execute(newTag)

        // Assert
        assertFalse(result.success)
        assertEquals(HttpStatusCode.BadRequest, result.statusCode)
        assertEquals("Tag met deze naam bestaat al", result.message)
        assertEquals(1, repository.getAll().size) // No new tag added
    }

    @Test
    fun `CreateTagUseCase - should fail when tag name exists with different case`() = runBlocking {
        // Arrange
        val tags = mutableListOf(Tag(id = 1, name = "Adventure"))
        val repository = FakeTagRepository(tags)
        val useCase = CreateTagUseCase(repository)
        val newTag = Tag(id = 2, name = "ADVENTURE")

        // Act
        val result = useCase.execute(newTag)

        // Assert
        assertFalse(result.success)
        assertEquals(HttpStatusCode.BadRequest, result.statusCode)
        assertEquals("Tag met deze naam bestaat al", result.message)
    }

    @Test
    fun `CreateTagUseCase - should create tag when different name`() = runBlocking {
        // Arrange
        val tags = mutableListOf(Tag(id = 1, name = "Adventure"))
        val repository = FakeTagRepository(tags)
        val useCase = CreateTagUseCase(repository)
        val newTag = Tag(id = 2, name = "Family")

        // Act
        val result = useCase.execute(newTag)

        // Assert
        assertTrue(result.success)
        assertEquals("Family", result.result?.name)
        assertEquals(2, repository.getAll().size)
    }
}
