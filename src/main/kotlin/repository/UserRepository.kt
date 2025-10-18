package repository

import dtos.UserRegisterDto
import dtos.toUser
import model.User

class UserRepository : CrudRepository<User> {

    private val users = mutableListOf<User>()

    override suspend fun getAll(): List<User> = users

    override suspend fun getById(id: Int): User? =
        users.find { it.id == id }

    override suspend fun getByQuery(predicate: (User) -> Boolean): List<User> {
        return users.filter(predicate)
    }

    override suspend fun create(entity: User): User {
      users.add(entity)
        return entity
    }

    override suspend fun update(id: Int, entity: User): User? {
        val index = users.indexOfFirst { it.id == id }
        return if (index != -1) {
            users[index] = entity
            entity
        } else null
    }

    override suspend fun delete(id: Int): Boolean =
        users.removeIf { it.id == id }
}


