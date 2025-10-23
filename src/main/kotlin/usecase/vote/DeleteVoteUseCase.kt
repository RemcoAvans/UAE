package usecase.vote

import com.example.core.ObjectResult
import com.example.repository.IActivityVoteRepository
import com.example.usecase.BaseInputUseCase
import repository.ActivityVoteRepository

class DeleteVoteUseCase(val repository: IActivityVoteRepository) : BaseInputUseCase<Int?, Boolean> {

    override suspend fun execute(input: Int?): ObjectResult<Boolean> {
        // check of id bestaat
        if (input == null || input <= 0) {
            return ObjectResult.fail("voteID is geen geldig getal")
        }
        val success = repository.delete(input)
        if (!success){
            return ObjectResult.notFound("vote niet gevonden")
        }
        return ObjectResult.success(true)
    }
}
