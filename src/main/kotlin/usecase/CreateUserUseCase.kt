//package com.example.usecase
//
//import com.example.core.ObjectResult
//import dtos.UserRegisterDto
//import dtos.toUser
//import io.ktor.utils.io.core.Input
//import model.User
//import repository.UserRepository
//import usecase.BaseUseCase
//
//class CreateUserUseCase(private val repository: UserRepository) : BaseInputUseCase<UserRegisterDto, User>{
//
//    override suspend fun execute(input: UserRegisterDto): ObjectResult<User> {
//        val user = input.toUser()
//        val result = repository.create(user)
//        return ObjectResult.success(result)
//
//
//
//    }
//}