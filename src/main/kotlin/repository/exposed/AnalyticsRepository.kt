package repository.exposed

import com.example.repository.IAnalyticsRepository
import model.SearchLog

open class AnalyticsRepository : IAnalyticsRepository {

    private val searchLogs: MutableList<SearchLog> = mutableListOf()
    private var searchLogIdCounter = 1

    open override suspend fun logSearch(
        categories: List<String>?,
        city: String?,
        minPrice: Int?,
        maxPrice: Int?,
        resultsCount: Int
    ): SearchLog {
        val log = SearchLog(
            id = searchLogIdCounter++,
            timestamp = System.currentTimeMillis(),
            categories = categories,
            city = city,
            minPrice = minPrice,
            maxPrice = maxPrice,
            resultsCount = resultsCount
        )
        searchLogs.add(log)
        return log
    }

    open override suspend fun getAllSearchLogs(): List<SearchLog> = searchLogs

    open override suspend fun getSearchLogsByDateRange(fromTimestamp: Long, toTimestamp: Long): List<SearchLog> {
        return searchLogs.filter { it.timestamp in fromTimestamp..toTimestamp }
    }
}
