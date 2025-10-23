package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class ActivityTag(
    val id: Int,
    val ActivityId: Int,
    val TagId: Int,
)