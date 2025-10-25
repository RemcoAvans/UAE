package com.example.repository

import com.example.model.AnalyticsData
import repository.CrudRepository

interface IAnalyticsDataRepository : CrudRepository<AnalyticsData> {
    suspend fun getByLocationId(locationId: Int): AnalyticsData?
}