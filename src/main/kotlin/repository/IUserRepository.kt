package com.example.repository

import com.example.config.PasswordHasher
import com.example.dtos.userDtos.userLoginDto
import model.User
import repository.CrudRepository

interface IUserRepository : CrudRepository<User> {

    fun login(loginDto: userLoginDto) : User?

    fun validate(user: User) : String
}