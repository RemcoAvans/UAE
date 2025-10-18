package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class ActivityTag(
    val ActivityId: Int,
    val TagId: Int,
)