package usecase.user

import com.example.core.ObjectResult
import com.example.usecase.BaseInputUseCase
import dtos.UserRegisterDto
import model.User
import repository.UserRepository

class RegisterUseCase(val repository: UserRepository) : BaseInputUseCase<UserRegisterDto, Int> {

    override suspend fun execute(input: UserRegisterDto): ObjectResult<Int> {
        // check if user exists
        if (input.email == "") {
            return ObjectResult.fail("Invalid email")
        }
        if (input.passwordHash == "") {
            return ObjectResult.fail("Invalid password")
        }

        val existingEmail = repository.getByQuery { it.email == input.email }
        if (existingEmail.isNotEmpty()) {
            return ObjectResult.fail("User already exists")
        }
//        val user = User(1, input.email)
//        repository.create(user)

        return ObjectResult.success(1)
    }
}