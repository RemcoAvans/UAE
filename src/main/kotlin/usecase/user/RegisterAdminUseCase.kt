package usecase.user

import com.example.core.ObjectResult
import com.example.repository.IUserRepository
import com.example.usecase.BaseInputUseCase
import dtos.UserRegisterDto
import dtos.toAdmin
import dtos.toUser
import model.User
import repository.UserRepository

class RegisterAdminUseCase (val repository: IUserRepository) : BaseInputUseCase<UserRegisterDto, User> {

    override suspend fun execute(input: UserRegisterDto): ObjectResult<User> {
        val user = input.toAdmin()
        val result : String = repository.validate(user)

        when (result) {
            "Invalid email" -> return ObjectResult.fail("Invalid Email")
            "Invalid password" -> return ObjectResult.fail("Invalid Password")
            "User already exists" -> return ObjectResult.fail("User already exists")
            "OK" -> {repository.create(user); return ObjectResult.success(user)}
            else -> return ObjectResult.fail("Unknown error")
        }


//



    }
}