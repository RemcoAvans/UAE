package com.example.repository.exposed

import com.example.data.models.LocationTable
import com.example.model.Location
import com.example.repository.ILocationRepository
import repository.CrudRepository
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

open class LocationRepository : ILocationRepository {

    private fun toLocation(row: ResultRow): Location =
        Location(
            id = row[LocationTable.id].value,
            latitude = row[LocationTable.latitude],
            longitude = row[LocationTable.longitude],
            city = row[LocationTable.city],
            country = row[LocationTable.country]
        )

    override suspend fun getAll(): List<Location> =
        DatabaseHelper.dbQuery {
            LocationTable
                .selectAll()
                .map(::toLocation)
        }

    override suspend fun getById(id: Int): Location? =
        DatabaseHelper.dbQuery {
            LocationTable
                .selectAll()
                .where { LocationTable.id eq id }
                .map(::toLocation)
                .singleOrNull()
        }

    override suspend fun getByQuery(
        predicate: (Location) -> Boolean
    ): List<Location> =
        getAll().filter(predicate)

    override suspend fun create(entity: Location): Location =
        DatabaseHelper.dbQuery {
            val id = LocationTable.insertAndGetId { row ->
                row[latitude] = entity.latitude
                row[longitude] = entity.longitude
                row[city] = entity.city
                row[country] = entity.country
            }

            entity.copy(id = id.value)
        }

    override suspend fun update(id: Int, entity: Location): Location? =
        DatabaseHelper.dbQuery {
            val updated = LocationTable.update(
                where = { LocationTable.id eq id }
            ) { row ->
                row[latitude] = entity.latitude
                row[longitude] = entity.longitude
                row[city] = entity.city
                row[country] = entity.country
            }

            if (updated > 0) entity.copy(id = id) else null
        }

    override suspend fun delete(id: Int): Boolean =
        DatabaseHelper.dbQuery {
            LocationTable.deleteWhere { LocationTable.id eq id } > 0
        }
}
