package usecase.vote

import com.example.core.ObjectResult
import com.example.usecase.BaseInputUseCase
import com.example.model.ActivityVote
import repository.ActivityVoteRepository

class GetVotesByActivityUseCase(val repository: ActivityVoteRepository) : BaseInputUseCase<Int?, List<ActivityVote>> {

    override suspend fun execute(input: Int?): ObjectResult<List<ActivityVote>> {
        // check of id bestaat
        if (input == null || input <= 0) {
            return ObjectResult.fail("activityId is ongeldig")
        }
        // get votes from repo
        val votes = repository.getByQuery {it.activityId == input }
        // check if votes exist
        if (votes.isEmpty()) {
            return ObjectResult.notFound("deze activiteit heeft 0 stemmen")
        }

        return ObjectResult.success(votes)
    }
}