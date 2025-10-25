package repository

import com.example.model.Tag

class TagRepository {

    private val tags = mutableListOf<Tag>()

    fun getAll(): List<Tag> = tags.toList()

    fun getById(id: Int): Tag? = tags.find { it.id == id }

    fun create(tag: Tag): Tag {
        val nextId = (tags.maxOfOrNull { it.id } ?: 0) + 1
        val newTag = tag.copy(id = nextId)
        tags.add(newTag)
        return newTag
    }

    fun update(tag: Tag): Boolean {
        val index = tags.indexOfFirst { it.id == tag.id }
        return if (index != -1) {
            tags[index] = tag
            true
        } else {
            false
        }
    }

    fun delete(id: Int): Boolean {
        return tags.removeIf { it.id == id }
    }
}
