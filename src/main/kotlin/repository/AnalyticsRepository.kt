package repository

import model.SearchLog

class AnalyticsRepository {

    private val searchLogs: MutableList<SearchLog> = mutableListOf()
    private var searchLogIdCounter = 1

    suspend fun logSearch(
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

    suspend fun getAllSearchLogs(): List<SearchLog> = searchLogs

    suspend fun getSearchLogsByDateRange(fromTimestamp: Long, toTimestamp: Long): List<SearchLog> {
        return searchLogs.filter { it.timestamp in fromTimestamp..toTimestamp }
    }
}
