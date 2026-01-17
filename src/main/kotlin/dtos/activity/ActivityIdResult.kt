package dtos.activity

import kotlinx.serialization.Serializable

@Serializable
data class ActivityIdResult(
    val activityIds: List<Int>
)