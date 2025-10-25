package usecase

import com.example.model.Tag
import repository.TagRepository

class TagUseCase(private val tagRepository: TagRepository) {

    fun getAllTags(): List<Tag> = tagRepository.getAll()

    fun createTag(name: String): Tag {
        val newTag = Tag(id = 0, name = name)
        return tagRepository.create(newTag)
    }

    fun updateTag(id: Int, name: String): Boolean {
        val existing = tagRepository.getById(id) ?: return false
        val updated = existing.copy(name = name)
        return tagRepository.update(updated)
    }

    fun deleteTag(id: Int): Boolean = tagRepository.delete(id)
}
