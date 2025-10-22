import com.example.model.Tag
import repository.CrudRepository

class TagRepository : CrudRepository<Tag> {

    private val tags = mutableListOf<Tag>()

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
}