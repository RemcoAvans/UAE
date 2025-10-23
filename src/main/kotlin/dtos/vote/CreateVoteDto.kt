package dtos.vote

data class CreateVoteDto(
    val activityId: Int,
    val userId: Int,
    val voteType: String,
    val activityType: String,
    val tagSnapshot: String,
    val positive: Boolean
)