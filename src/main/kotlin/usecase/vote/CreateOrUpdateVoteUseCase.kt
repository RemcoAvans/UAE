package usecase.vote

import com.example.core.ObjectResult
import com.example.model.ActivityVote
import com.example.usecase.BaseInputUseCase
import dtos.vote.CreateVoteDto
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import repository.ActivityVoteRepository

class CreateOrUpdateVoteUseCase(private val repository: ActivityVoteRepository)
    : BaseInputUseCase<CreateVoteDto, Int> {

    override suspend fun execute(input: CreateVoteDto): ObjectResult<Int> {
        if (input.activityId <= 0 || input.userId <= 0) {
            return ObjectResult.fail("Input is invalid.")
        }

        val existingVote = repository.getByQuery {
            it.userId == input.userId && it.activityId == input.activityId
        }.firstOrNull()

        if (existingVote != null) {
            val updatedVote = existingVote.copy()
            val success = repository.update(updatedVote)
            return if (success) ObjectResult.success(updatedVote.id)
            else ObjectResult.fail("Could not update vote ${updatedVote.id}")
        } else {
            val today: LocalDate = Clock.System.now()
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .date
            val newVote = ActivityVote(
                id = 0,
                activityId = input.activityId,
                userId = input.userId,
                createAt = today,
                positive = input.positive
            )
            val created = repository.create(newVote)
            return ObjectResult.success(created.id)
        }
    }
}
