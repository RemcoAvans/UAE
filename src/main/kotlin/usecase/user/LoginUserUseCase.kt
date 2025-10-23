package com.example.usecase.user

import com.example.core.ObjectResult
import com.example.dtos.userDtos.userLoginDto
import com.example.repository.IUserRepository
import com.example.usecase.BaseInputUseCase
import model.User
import repository.UserRepository

class LoginUserUseCase(val repository: IUserRepository) : BaseInputUseCase<userLoginDto, User> {
    override suspend fun execute(input: userLoginDto): ObjectResult<User> {
        val user : User? = repository.login(input)

      return if(user != null) {
          ObjectResult.success(user)
      } else {
          ObjectResult.fail("User not found ,are you sure it exists.")
      }

    }
}