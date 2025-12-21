package com.example.repository.exposed

import com.example.data.models.TagTable
import com.example.model.Tag
import repository.CrudRepository
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

open class TagRepository : CrudRepository<Tag> {

    private fun toTag(row: ResultRow): Tag =
        Tag(
            id = row[TagTable.id].value,
            name = row[TagTable.name]
        )

    override suspend fun getAll(): List<Tag> =
        DatabaseHelper.dbQuery {
            TagTable
                .selectAll()
                .map(::toTag)
        }

    override suspend fun getById(id: Int): Tag? =
        DatabaseHelper.dbQuery {
            TagTable
                .selectAll()
                .where { TagTable.id eq id }
                .map(::toTag)
                .singleOrNull()
        }

    override suspend fun getByQuery(
        predicate: (Tag) -> Boolean
    ): List<Tag> =
        getAll().filter(predicate)

    override suspend fun create(entity: Tag): Tag =
        DatabaseHelper.dbQuery {
            val id = TagTable.insertAndGetId { row ->
                row[name] = entity.name
            }
            entity.copy(id = id.value)
        }

    override suspend fun update(id: Int, entity: Tag): Tag? =
        DatabaseHelper.dbQuery {
            val updated = TagTable.update(
                where = { TagTable.id eq id }
            ) { row ->
                row[name] = entity.name
            }

            if (updated > 0) entity else null
        }

    override suspend fun delete(id: Int): Boolean =
        DatabaseHelper.dbQuery {
            TagTable.deleteWhere { TagTable.id eq id } > 0
        }
}
