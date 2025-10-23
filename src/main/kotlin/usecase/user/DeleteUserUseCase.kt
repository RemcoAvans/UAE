package usecase.user

import com.example.core.ObjectResult
import com.example.repository.IUserRepository
import com.example.usecase.BaseInputUseCase
import repository.UserRepository

class DeleteUserUseCase(val repository: IUserRepository) : BaseInputUseCase<Int?, Boolean> {

    override suspend fun execute(input: Int?): ObjectResult<Boolean> {
        // check if id is valide
        if (input == null || input < 0) {
            return ObjectResult.fail("ID is niet valide")
        }
        // remove id
        val success = repository.delete(input)
        if (!success){
            return ObjectResult.notFound("gebruiker niet gevonden")
        }
        return ObjectResult.success(true)
    }
}
