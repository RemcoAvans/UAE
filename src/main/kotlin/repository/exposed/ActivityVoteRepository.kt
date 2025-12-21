package repository.exposed

import com.example.data.models.ActivityVoteTable
import com.example.model.ActivityVote
import com.example.repository.IActivityVoteRepository
import com.example.repository.exposed.DatabaseHelper
import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import kotlin.Int

open class ActivityVoteRepository : IActivityVoteRepository {

    private fun toActivityVote(row: ResultRow): ActivityVote =
        ActivityVote(
            id = row[ActivityVoteTable.id].value,
            activityId = row[ActivityVoteTable.activityId],
            userId = row[ActivityVoteTable.userId],
            createAt = LocalDate.parse(row[ActivityVoteTable.createAt]),
            positive = row[ActivityVoteTable.positive],
        )

    //To delete
    private val votes = mutableListOf<ActivityVote>()

    override suspend fun getAll(): List<ActivityVote> = DatabaseHelper.dbQuery {
        ActivityVoteTable
            .selectAll()
            .map(::toActivityVote)
    }

    override suspend fun getById(id: Int): ActivityVote? = DatabaseHelper.dbQuery  {
        ActivityVoteTable
            .selectAll().where { ActivityVoteTable.id eq id }.toList()
            .map(::toActivityVote)
            .singleOrNull()
    }

    open override  suspend fun getByQuery(predicate: (ActivityVote) -> Boolean): List<ActivityVote>  =
        getAll().filter(predicate)

    override suspend fun create(entity: ActivityVote): ActivityVote = DatabaseHelper.dbQuery {
        val id = ActivityVoteTable.insertAndGetId { row ->
            row[activityId] = entity.activityId
            row[userId] = entity.userId
            row[createAt] = entity.createAt.toString()
            row[positive] = entity.positive
        }
        entity.copy(id = id.value)
    }

    override suspend fun update(entity: ActivityVote): Boolean = DatabaseHelper.dbQuery {
        val updated = ActivityVoteTable.update({ ActivityVoteTable.id eq entity.id }) { row ->
            row[activityId] = entity.activityId
            row[userId] = entity.userId
            row[createAt] = entity.createAt.toString()
            row[positive] = entity.positive
        }
        updated > 0
    }

    override suspend fun delete(id: Int): Boolean = DatabaseHelper.dbQuery {
        ActivityVoteTable.deleteWhere { ActivityVoteTable.id eq id } > 0
    }
}
