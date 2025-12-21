package repository.exposed

import com.example.data.models.AnalyticsDataTable
import com.example.model.AnalyticsData
import com.example.repository.IAnalyticsDataRepository
import com.example.repository.exposed.DatabaseHelper
import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

open class AnalyticsDataRepository : IAnalyticsDataRepository {

    private fun toAnalyticsData(row: ResultRow): AnalyticsData =
        AnalyticsData(
            id = row[AnalyticsDataTable.id].value,
            locationId = row[AnalyticsDataTable.locationId],
            activityCount = row[AnalyticsDataTable.activityCount],
            lastUpdated = LocalDate.parse(row[AnalyticsDataTable.lastUpdated])
        )

    override suspend fun getAll(): List<AnalyticsData> = DatabaseHelper.dbQuery {
        AnalyticsDataTable
            .selectAll()
            .map(::toAnalyticsData)
    }

    override suspend fun getById(id: Int): AnalyticsData? = DatabaseHelper.dbQuery {
        AnalyticsDataTable
            .selectAll()
            .where { AnalyticsDataTable.id eq id }
            .map(::toAnalyticsData)
            .singleOrNull()
    }

    override suspend fun getByQuery(
        predicate: (AnalyticsData) -> Boolean
    ): List<AnalyticsData> =
        getAll().filter(predicate)

    override suspend fun create(entity: AnalyticsData): AnalyticsData =
        DatabaseHelper.dbQuery {
            val id = AnalyticsDataTable.insertAndGetId { row ->
                row[locationId] = entity.locationId
                row[activityCount] = entity.activityCount
                row[lastUpdated] = entity.lastUpdated.toString()
            }
            entity.copy(id = id.value)
        }

    override suspend fun update(
        id: Int,
        entity: AnalyticsData
    ): AnalyticsData? = DatabaseHelper.dbQuery {
        val updated = AnalyticsDataTable.update(
            where = { AnalyticsDataTable.id eq id }
        ) { row ->
            row[locationId] = entity.locationId
            row[activityCount] = entity.activityCount
            row[lastUpdated] = entity.lastUpdated.toString()
        }

        if (updated > 0) entity else null
    }

    override suspend fun delete(id: Int): Boolean = DatabaseHelper.dbQuery {
        AnalyticsDataTable.deleteWhere { AnalyticsDataTable.id eq id } > 0
    }

    override suspend fun getByLocationId(locationId: Int): AnalyticsData? =
        DatabaseHelper.dbQuery {
            AnalyticsDataTable
                .selectAll()
                .where { AnalyticsDataTable.locationId eq locationId }
                .map(::toAnalyticsData)
                .singleOrNull()
        }
}
