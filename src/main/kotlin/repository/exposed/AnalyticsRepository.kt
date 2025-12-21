package repository.exposed

import com.example.data.models.SearchLogTable
import com.example.repository.IAnalyticsRepository
import com.example.repository.exposed.DatabaseHelper
import model.SearchLog
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll

open class AnalyticsRepository : IAnalyticsRepository {

    private fun toSearchLog(row: ResultRow): SearchLog =
        SearchLog(
            id = row[SearchLogTable.id].value,
            timestamp = row[SearchLogTable.timestamp],
            categories = null, // (nog niet opgeslagen)
            city = row[SearchLogTable.city],
            minPrice = row[SearchLogTable.minPrice],
            maxPrice = row[SearchLogTable.maxPrice],
            resultsCount = row[SearchLogTable.resultsCount]
        )

    override suspend fun logSearch(
        categories: List<String>?,
        city: String?,
        minPrice: Int?,
        maxPrice: Int?,
        resultsCount: Int
    ): SearchLog = DatabaseHelper.dbQuery {
        val id = SearchLogTable.insertAndGetId { row ->
            row[timestamp] = System.currentTimeMillis()
            row[SearchLogTable.city] = city
            row[SearchLogTable.minPrice] = minPrice
            row[SearchLogTable.maxPrice] = maxPrice
            row[SearchLogTable.resultsCount] = resultsCount
        }

        SearchLog(
            id = id.value,
            timestamp = System.currentTimeMillis(),
            categories = categories,
            city = city,
            minPrice = minPrice,
            maxPrice = maxPrice,
            resultsCount = resultsCount
        )
    }

    override suspend fun getAllSearchLogs(): List<SearchLog> =
        DatabaseHelper.dbQuery {
            SearchLogTable
                .selectAll()
                .map(::toSearchLog)
        }

    override suspend fun getSearchLogsByDateRange(
        fromTimestamp: Long,
        toTimestamp: Long
    ): List<SearchLog> = DatabaseHelper.dbQuery {
        SearchLogTable
            .selectAll()
            .where { SearchLogTable.timestamp.between(fromTimestamp, toTimestamp) }
            .map(::toSearchLog)
    }
}