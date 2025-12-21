package com.example.repository.exposed

import com.example.data.models.ActivityTagTable
import com.example.model.ActivityTag
import com.example.repository.IActivityTagRepository
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update

open class ActivityTagRepository : IActivityTagRepository {

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    private fun toActivityTag(row: ResultRow): ActivityTag =
        ActivityTag(
            id = row[ActivityTagTable.id].value,
            ActivityId = row[ActivityTagTable.ActivityId],
            TagId = row[ActivityTagTable.TagId]
        )

    override suspend fun getAll(): List<ActivityTag> = dbQuery {
        ActivityTagTable
            .selectAll()
            .map { toActivityTag(it) }
    }

    override suspend fun getById(id: Int): ActivityTag? = dbQuery {
        ActivityTagTable
            .selectAll().where { ActivityTagTable.id eq id }
            .map { toActivityTag(it) }
            .singleOrNull()
    }

    override suspend fun getByQuery(
        predicate: (ActivityTag) -> Boolean
    ): List<ActivityTag> =
        getAll().filter(predicate)
    // ↑ predicate kan niet direct naar SQL vertaald worden

    override suspend fun create(entity: ActivityTag): ActivityTag = dbQuery {
        val insert = ActivityTagTable.insert {
            it[ActivityId] = entity.ActivityId
            it[TagId] = entity.TagId
        }

        entity.copy(
            id = insert[ActivityTagTable.id].value
        )
    }

    override suspend fun update(id: Int, entity: ActivityTag): ActivityTag? = dbQuery {
        val updated = ActivityTagTable.update(
            where = { ActivityTagTable.id eq id }
        ) {
            it[ActivityId] = entity.ActivityId
            it[TagId] = entity.TagId
        }

        if (updated > 0) getById(id) else null
    }

    override suspend fun delete(id: Int): Boolean = dbQuery {
        ActivityTagTable.deleteWhere { ActivityTagTable.id eq id } > 0
    }
}