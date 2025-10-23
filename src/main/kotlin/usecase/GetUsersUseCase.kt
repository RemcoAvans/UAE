package usecase

import com.example.core.ObjectResult
import com.example.repository.IUserRepository
import model.User
import repository.UserRepository
import kotlin.collections.List

class GetUsersUseCase(private val repository: IUserRepository) : BaseUseCase<List<User>> {

    override suspend fun execute(): ObjectResult<List<User>> {
        val users = repository.getAll()

        return if (users.isNotEmpty()) {
            ObjectResult.success(users)
        } else {
            ObjectResult.notFound("Geen gebruikers gevonden")
        }
    }
}
