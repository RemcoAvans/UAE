package repository

import com.example.model.ActivityVote
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class ActivityVoteRepository {

    private val votes = mutableListOf<ActivityVote>()

    suspend fun getAll(): List<ActivityVote> = votes

    suspend fun getById(id: Int): ActivityVote? = votes.find { it.id == id }

    suspend fun getByQuery(predicate: (ActivityVote) -> Boolean): List<ActivityVote> =
        votes.filter(predicate)

    suspend fun create(entity: ActivityVote): ActivityVote {
        val nextId = (votes.maxOfOrNull { it.id } ?: 0) + 1
        val newVote = entity.copy(
            id = nextId,
            createdAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        )
        votes.add(newVote)
        return newVote
    }

    suspend fun update(entity: ActivityVote): Boolean {
        val index = votes.indexOfFirst { it.id == entity.id }
        if (index != -1) {
            votes[index] = entity
            return true
        }
        return false
    }

    suspend fun delete(id: Int): Boolean {
        return votes.removeIf { it.id == id }
    }
}
