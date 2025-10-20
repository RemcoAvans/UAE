package repository

import model.ActivityVote
import model.User

class ActivityVoteRepository : CrudRepository<ActivityVote> {

    private val ActivityVotes: MutableList<ActivityVote> = mutableListOf() // een legen lijst om in memory data op te slaan


    override suspend fun getAll(): List<ActivityVote>  = ActivityVotes


    override suspend fun getById(id: Int): ActivityVote? {
        return ActivityVotes.find { it.id == id }
    }

    override suspend fun getByQuery(predicate: (ActivityVote) -> Boolean): List<ActivityVote> {
        return ActivityVotes.filter(predicate)
    }

    override suspend fun create(entity: ActivityVote): ActivityVote {
        ActivityVotes.add(entity)
        return entity
    }

    override suspend fun update(id: Int, entity: ActivityVote): ActivityVote? {
        val index = ActivityVotes.indexOfFirst { it.id == id }
        if (index == -1) return null
        ActivityVotes[index] = entity.copy(id = id)
        return ActivityVotes[index]
    }

    override suspend fun delete(id: Int): Boolean =
        ActivityVotes.removeIf { it.id == id }
}