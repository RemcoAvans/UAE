package com.example.usecase

import com.example.core.ObjectResult
import io.ktor.utils.io.core.Input
import model.User
import repository.UserRepository
import usecase.BaseUseCase

class CreateUserUseCase(private val repository: UserRepository) : BaseInputUseCase<User, User>{

    override suspend fun execute(input: User): ObjectResult<User> {
        val result = repository.create(input)
        return ObjectResult.success(result)



    }
}