package routing

import com.example.baseRouter.BaseRouter.badRequest
import com.example.baseRouter.BaseRouter.handle
import com.example.usecase.CreateActivityUseCase
import com.example.usecase.GetActivitiesUseCase
import com.example.usecase.GetActivityUseCase
import com.example.usecase.activity.DeleteActivityUseCase
import com.example.usecase.activity.FilterActivitiesUseCase
import com.example.usecase.activity.GetFeaturedActivitiesUseCase
import com.example.usecase.activity.PromoteActivityUseCase
import com.example.usecase.activity.UnpromoteActivityUseCase
import dtos.ActivityFilterDto
import io.ktor.server.request.receive
import io.ktor.server.routing.*
import model.Activity

import repository.ActivityRepository

fun Route.activityRoutes() {
    val repo = ActivityRepository()
    val createActivityUseCase = CreateActivityUseCase(repo)
    val getActivitiesUseCase = GetActivitiesUseCase(repo)
    val filterActivitiesUseCase = FilterActivitiesUseCase(repo)
    val getActivityUseCase = GetActivityUseCase(repo)
    val deleteActivity = DeleteActivityUseCase(repo)
    val promoteActivityUseCase = PromoteActivityUseCase(repo)
    val unpromoteActivityUseCase = UnpromoteActivityUseCase(repo)
    val getFeaturedActivitiesUseCase = GetFeaturedActivitiesUseCase(repo)
    var aantal = 0

    route("/activities") {

        get() {
            val result = getActivitiesUseCase.execute()
            call.handle(result)
        }

        post("/filter") {
            val filter = call.receive<ActivityFilterDto>()
            val result = filterActivitiesUseCase.execute(filter)
            call.handle(result)
        }

        get ("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null){
                call.badRequest("Ongeldig of geen id")
                return@get
            }

            val result = getActivityUseCase.execute(id)
            call.handle(result)
        }

        delete ("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val result = deleteActivity.execute(id)
            call.handle(result)
        }

        // GET /activities/featured - Ophalen van uitgelichte activiteiten
        get("/featured") {
            val result = getFeaturedActivitiesUseCase.execute()
            call.handle(result)
        }
    }

    // Admin endpoints voor promoten/depromoten
    route("/admin/activities") {
        // POST /admin/activities/{id}/promote
        post("/{id}/promote") {
            val id = call.parameters["id"]?.toIntOrNull()
            val result = promoteActivityUseCase.execute(id)
            call.handle(result)
        }

        // POST /admin/activities/{id}/unpromote
        post("/{id}/unpromote") {
            val id = call.parameters["id"]?.toIntOrNull()
            val result = unpromoteActivityUseCase.execute(id)
            call.handle(result)
        }
    }
}
