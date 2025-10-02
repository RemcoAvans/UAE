package usecase

import com.example.core.ObjectResult
import model.User
import repository.UserRepository

class GetUsersUseCase(private val repository: UserRepository) : BaseUseCase<List<User>> {

    override suspend fun execute(): ObjectResult<List<User>> {
        val users = repository.getAll()

        return if (users.isNotEmpty()) {
            ObjectResult.success(users)
        } else {
            ObjectResult.notFound("Geen gebruikers gevonden")
        }
    }
}
