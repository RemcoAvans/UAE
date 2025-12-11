package repository.exposed

import com.example.model.ActivityVote
import com.example.repository.IActivityVoteRepository
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

open class ActivityVoteRepository : IActivityVoteRepository {

    private val votes = mutableListOf<ActivityVote>()

    override suspend fun getAll(): List<ActivityVote> = votes

    override suspend fun getById(id: Int): ActivityVote? = votes.find { it.id == id }

    open override  suspend fun getByQuery(predicate: (ActivityVote) -> Boolean): List<ActivityVote> =
        votes.filter(predicate)

    override suspend fun create(entity: ActivityVote): ActivityVote {
        val nextId = (votes.maxOfOrNull { it.id } ?: 0) + 1
        val today: LocalDate = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
        val newVote = entity.copy(
            id = nextId,
            createAt = today
        )
        votes.add(newVote)
        return newVote
    }

    override suspend fun update(entity: ActivityVote): Boolean {
        val index = votes.indexOfFirst { it.id == entity.id }
        if (index != -1) {
            votes[index] = entity
            return true
        }
        return false
    }

    override suspend fun delete(id: Int): Boolean {
        return votes.removeIf { it.id == id }
    }
}
