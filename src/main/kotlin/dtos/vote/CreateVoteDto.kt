package dtos.vote

import kotlinx.serialization.Serializable

@Serializable
data class CreateVoteDto(
    val activityId: Int,
    val userId: Int,
    val positive: Boolean
)