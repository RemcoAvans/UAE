package com.example.dtos.userDtos

import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

@Serializable
data class userLoginDto(
    val loginName : String,
    val password : String
)