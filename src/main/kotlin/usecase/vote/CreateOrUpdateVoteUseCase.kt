package usecase.vote

import com.example.core.ObjectResult
import com.example.model.ActivityVote
import com.example.usecase.BaseInputUseCase
import dtos.CreateVoteDto
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import repository.ActivityVoteRepository

class CreateOrUpdateVoteUseCase(private val repository: ActivityVoteRepository)
    : BaseInputUseCase<CreateVoteDto, Int> {

    override suspend fun execute(input: CreateVoteDto): ObjectResult<Int> {
        if (input.activityId <= 0 || input.userId <= 0) {
            return ObjectResult.fail("activityId of userId is ongeldig")
        }

        val existingVote = repository.getByQuery {
            it.userId == input.userId && it.activityId == input.activityId
        }.firstOrNull()

        if (existingVote != null) {
            val updatedVote = existingVote.copy(
                voteType = input.voteType,
                activityType = input.activityType,
                tagSnapshot = input.tagSnapshot
            )
            val success = repository.update(updatedVote)
            return if (success) ObjectResult.success(updatedVote.id)
            else ObjectResult.fail("Kon stem niet updaten")
        } else {
            val newVote = ActivityVote(
                id = 0,
                activityId = input.activityId,
                userId = input.userId,
                voteType = input.voteType,
                activityType = input.activityType,
                tagSnapshot = input.tagSnapshot,
                createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            )
            val created = repository.create(newVote)
            return ObjectResult.success(created.id)
        }
    }
}
