package repository

import model.SearchLog

open class AnalyticsRepository {

    private val searchLogs: MutableList<SearchLog> = mutableListOf()
    private var searchLogIdCounter = 1

    open suspend fun logSearch(
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

    open suspend fun getAllSearchLogs(): List<SearchLog> = searchLogs

    open suspend fun getSearchLogsByDateRange(fromTimestamp: Long, toTimestamp: Long): List<SearchLog> {
        return searchLogs.filter { it.timestamp in fromTimestamp..toTimestamp }
    }
}
