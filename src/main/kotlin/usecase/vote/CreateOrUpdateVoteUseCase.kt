package usecase.vote

import com.example.core.ObjectResult
import com.example.usecase.BaseInputUseCase
import com.example.model.ActivityVote
import dtos.CreateVoteDto
import kotlinx.datetime.LocalDate
import repository.ActivityVoteRepository

class CreateOrUpdateVoteUseCase(
    private val repository: ActivityVoteRepository
) : BaseInputUseCase<CreateVoteDto, Int> {

    override suspend fun execute(input: CreateVoteDto): ObjectResult<Int> {
        // check of id bestaat, user en acitviteit
        if (input.activityId <= 0 || input.userId <= 0) {
            return ObjectResult.fail("activiteit- of user id ongeldig")
        }

        // check if user already voted for the activity
        val existingVote = repository.getByQuery {
            it.userId == input.userId && it.activityId == input.activityId
        }.firstOrNull()

        if (existingVote != null) {
            // update the vote if it exists
            val updatedVote = existingVote.copy(
                voteType = input.voteType,
                activityType = input.activityType,
                tagSnapshot = input.tagSnapshot
            )
            repository.update(updatedVote.id, updatedVote)

            return ObjectResult.success(updatedVote.id)
        } else {
            // make new vote
            val newVote = ActivityVote(
                id = 0,
                activityId = input.activityId,
                userId = input.userId,
                voteType = input.voteType,
                activityType = input.activityType,
                tagSnapshot = input.tagSnapshot,
                createAt = LocalDate.now()
            )

            val generatedId = repository.create(newVote)
            return ObjectResult.success(generatedId)
        }
    }
}
