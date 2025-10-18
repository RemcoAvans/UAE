package com.example.usecase.analyticsdata

import com.example.core.ObjectResult
import com.example.model.AnalyticsData
import com.example.usecase.BaseInputUseCase
import repository.ActivityRepository
import repository.AnalyticsDataRepository

class GetAnalyticsDataByActivityUseCase(
    private val analyticsDataRepository: AnalyticsDataRepository,
    private val activityRepository: ActivityRepository
) : BaseInputUseCase<Int?, AnalyticsData> {

    override suspend fun execute(input: Int?): ObjectResult<AnalyticsData> {
        if (input == null || input < 0) {
            return ObjectResult.fail("Ongeldige activity ID")
        }

        // Haal de activity op
        val activity = activityRepository.getById(input)
        if (activity == null) {
            return ObjectResult.notFound("Activity niet gevonden")
        }

        // Haal analytics data op voor de location van deze activity
        val analyticsData = analyticsDataRepository.getByLocationId(activity.locationId)
        return if (analyticsData != null) {
            ObjectResult.success(analyticsData)
        } else {
            ObjectResult.notFound("Geen analytics data gevonden voor deze activity locatie")
        }
    }
}
