package repository

import com.example.config.PasswordHasher
import com.example.core.ObjectResult
import com.example.dtos.userDtos.userLoginDto
import com.example.repository.IUserRepository
import dtos.UserRegisterDto
import dtos.toUser
import model.User

open class UserRepository : IUserRepository {

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

    override fun login(loginDto: userLoginDto) : User?{
        val loginName = loginDto.loginName
        val loginPassword = loginDto.password

        val user = users.find { it.username == loginName || it.email == loginName }
        return if (user != null && PasswordHasher.verify(loginPassword, user.passwordHash)) {
            user
        } else {
            null
        }
    }

    override fun validate(user: User) : String{
        if (user.email == "") {
            return "Invalid email"
        }
        if (user.passwordHash == "") {
            return "Invalid password"
        }
        val exists = users.any { it.username == user.username }
        if (exists) {
            return "User already exists"
        }
        return "OK"
    }
}


