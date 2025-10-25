package com.example.repository

import model.SearchLog

interface IAnalyticsRepository {

    suspend fun logSearch(
        categories: List<String>?,
        city: String?,
        minPrice: Int?,
        maxPrice: Int?,
        resultsCount: Int
    ): SearchLog

    suspend fun getAllSearchLogs(): List<SearchLog>

    suspend fun getSearchLogsByDateRange(fromTimestamp: Long, toTimestamp: Long): List<SearchLog>
}