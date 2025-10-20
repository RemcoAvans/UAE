package usecase.user

import com.example.core.ObjectResult
import com.example.usecase.BaseInputUseCase
import repository.UserRepository

class DeleteUserUseCase(val repository: UserRepository) : BaseInputUseCase<Int?, Boolean> {

    override suspend fun execute(input: Int?): ObjectResult<Boolean> {

        if (input == null || input < 0) {
            return ObjectResult.fail("ID is not valid")
        }

        val success = repository.delete(input)
        if (!success){
            return ObjectResult.notFound("User not found")
        }
        return ObjectResult.success(true)
    }
}
