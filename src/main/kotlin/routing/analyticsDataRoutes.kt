package com.example.routing

import com.example.baseRouter.BaseRouter.badRequest
import com.example.baseRouter.BaseRouter.handle
import com.example.usecase.analyticsdata.GetAllAnalyticsDataUseCase
import com.example.usecase.analyticsdata.GetAnalyticsDataByActivityUseCase
import io.ktor.server.routing.*
import repository.ActivityRepository
import repository.AnalyticsDataRepository

fun Route.analyticsDataRoutes(
    analyticsDataRepo: AnalyticsDataRepository,
    activityRepo: ActivityRepository
) {

    val getAllAnalyticsDataUseCase = GetAllAnalyticsDataUseCase(analyticsDataRepo)
    val getAnalyticsDataByActivityUseCase = GetAnalyticsDataByActivityUseCase(analyticsDataRepo, activityRepo)

    route("/analyticsData") {

        // GET /analyticsData - Alle analytics data ophalen
        get {
            val result = getAllAnalyticsDataUseCase.execute()
            call.handle(result)
        }

        // GET /analyticsData/{activityId} - Analytics data voor een specifieke activity (via location)
        get("/{activityId}") {
            val activityId = call.parameters["activityId"]?.toIntOrNull()
            if (activityId == null) {
                call.badRequest("Ongeldige of geen activity ID")
                return@get
            }
            val result = getAnalyticsDataByActivityUseCase.execute(activityId)
            call.handle(result)
        }
    }
}