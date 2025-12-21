package repository.exposed

import com.example.config.PasswordHasher
import com.example.data.models.UserTable
import com.example.dtos.userDtos.userLoginDto
import com.example.repository.IUserRepository
import com.example.repository.exposed.DatabaseHelper
import kotlinx.datetime.LocalDate
import model.User
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

open class UserRepository : IUserRepository {

    private fun toUser(row: ResultRow): User =
        User(
            id = row[UserTable.id].value,
            firstName = row[UserTable.firstName],
            lastName = row[UserTable.lastName],
            username = row[UserTable.username],
            email = row[UserTable.email],
            passwordHash = row[UserTable.passwordHash],
            role = row[UserTable.role],
            createdAt = LocalDate.parse(row[UserTable.createdAt])
        )

    override suspend fun getAll(): List<User> = DatabaseHelper.dbQuery {
        UserTable
            .selectAll()
            .map(::toUser)
    }

    override suspend fun getById(id: Int): User? = DatabaseHelper.dbQuery {
        UserTable
            .selectAll()
            .where { UserTable.id eq id }
            .map(::toUser)
            .singleOrNull()
    }

    override suspend fun getByQuery(predicate: (User) -> Boolean): List<User> =
        getAll().filter(predicate)

    override suspend fun create(entity: User): User = DatabaseHelper.dbQuery {
        val id = UserTable.insertAndGetId { row ->
            row[firstName] = entity.firstName
            row[lastName] = entity.lastName
            row[username] = entity.username
            row[email] = entity.email
            row[passwordHash] = entity.passwordHash
            row[role] = entity.role
            row[createdAt] = entity.createdAt.toString()
        }
        entity.copy(id = id.value)
    }

    override suspend fun update(id: Int, entity: User): User? = DatabaseHelper.dbQuery {
        val updated = UserTable.update({ UserTable.id eq id }) { row ->
            row[firstName] = entity.firstName
            row[lastName] = entity.lastName
            row[username] = entity.username
            row[email] = entity.email
            row[passwordHash] = entity.passwordHash
            row[role] = entity.role
            row[createdAt] = entity.createdAt.toString()
        }
        if (updated > 0) entity.copy(id = id) else null
    }

    override suspend fun delete(id: Int): Boolean = DatabaseHelper.dbQuery {
        UserTable.deleteWhere { UserTable.id eq id } > 0
    }

    override suspend fun login(loginDto: userLoginDto): User? =
        DatabaseHelper.dbQuery {
            val loginName = loginDto.loginName
            val loginPassword = loginDto.password

            val user = UserTable
                .selectAll()
                .where {
                    (UserTable.username eq loginName) or
                            (UserTable.email eq loginName)
                }
                .map(::toUser)
                .singleOrNull()

            if (user != null && PasswordHasher.verify(loginPassword, user.passwordHash)) {
                user
            } else {
                null
            }
        }

    override suspend fun validate(user: User): String =
        DatabaseHelper.dbQuery {
            if (user.email.isBlank()) {
                return@dbQuery "Invalid email"
            }

            if (user.passwordHash.isBlank()) {
                return@dbQuery "Invalid password"
            }

            val exists = UserTable
                .selectAll()
                .where { UserTable.username eq user.username }
                .count() > 0

            if (exists) {
                return@dbQuery "User already exists"
            }

            "OK"
        }
}
