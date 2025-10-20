package usecase.user

import com.example.core.ObjectResult
import com.example.usecase.BaseInputUseCase
import dtos.UserRegisterDto
import dtos.toUser
import model.User
import repository.UserRepository

class RegisterUseCase(val repository: UserRepository) : BaseInputUseCase<UserRegisterDto, User> {

    override suspend fun execute(input: UserRegisterDto): ObjectResult<User> {
        val user = input.toUser()
        if (user.email == "") {
            return ObjectResult.fail("Invalid email")
        }
        if (user.passwordHash == "") {
            return ObjectResult.fail("Invalid password")
        }

        val userName = repository.getByQuery { it.username == input.userName }
        if (userName.isNotEmpty()) {
            return ObjectResult.fail("User already exists")
        }
//
        repository.create(user)

        return ObjectResult.success(user)
    }
}