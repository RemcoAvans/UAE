package routing

import com.example.baseRouter.BaseRouter.handle
import com.example.repository.IActivityRepository
import com.example.repository.IAnalyticsRepository
import com.example.usecase.analytics.GetActivityStatisticsUseCase
import com.example.usecase.analytics.GetActivityTrendsUseCase
import com.example.usecase.analytics.GetSearchAnalyticsUseCase
import io.ktor.server.routing.*
import repository.ActivityRepository
import repository.AnalyticsRepository

fun Route.analyticsRoutes(
    activityRepo: IActivityRepository,
    analyticsRepo: IAnalyticsRepository
) {

    val getActivityTrendsUseCase = GetActivityTrendsUseCase(activityRepo, analyticsRepo)
    val getSearchAnalyticsUseCase = GetSearchAnalyticsUseCase(analyticsRepo)
    val getActivityStatisticsUseCase = GetActivityStatisticsUseCase(activityRepo)

    route("/admin/analytics") {

        // GET /admin/analytics/trends?limit=10
        get("/trends") {
            val limit = call.parameters["limit"]?.toIntOrNull()
            val result = getActivityTrendsUseCase.execute(limit)
            call.handle(result)
        }

        // GET /admin/analytics/searches
        get("/searches") {
            val result = getSearchAnalyticsUseCase.execute()
            call.handle(result)
        }

        // GET /admin/analytics/activities
        get("/activities") {
            val result = getActivityStatisticsUseCase.execute()
            call.handle(result)
        }

        // GET /admin/analytics/overview - Combinatie van alle analytics
        get("/overview") {
            val trendsResult = getActivityTrendsUseCase.execute(5)
            val searchesResult = getSearchAnalyticsUseCase.execute()
            val activitiesResult = getActivityStatisticsUseCase.execute()

            if (trendsResult.success && searchesResult.success && activitiesResult.success) {
                call.handle(com.example.core.ObjectResult.success<Map<String, Any?>>(
                    mapOf(
                        "trends" to trendsResult.result,
                        "searches" to searchesResult.result,
                        "activities" to activitiesResult.result
                    )
                ))
            } else {
                call.handle(com.example.core.ObjectResult.fail<Map<String, Any?>>("Kon niet alle analytics ophalen"))
            }
        }
    }
}
