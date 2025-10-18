package com.example.usecase.analyticsdata

import com.example.core.ObjectResult
import com.example.model.AnalyticsData
import repository.AnalyticsDataRepository
import usecase.BaseUseCase

class GetAllAnalyticsDataUseCase(private val repository: AnalyticsDataRepository) : BaseUseCase<List<AnalyticsData>> {

    override suspend fun execute(): ObjectResult<List<AnalyticsData>> {
        val data = repository.getAll()
        return if (data.isNotEmpty()) {
            ObjectResult.success(data)
        } else {
            ObjectResult.notFound("Geen analytics data gevonden")
        }
    }
}
