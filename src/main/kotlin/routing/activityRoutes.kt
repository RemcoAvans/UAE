package routing

import com.example.Dtos.ActivityFilterDto
import com.example.baseRouter.BaseRouter.badRequest
import com.example.baseRouter.BaseRouter.handle
import com.example.core.ObjectResult
import com.example.usecase.CreateActivityUseCase
import com.example.usecase.GetActivitiesUseCase
import com.example.usecase.GetActivityUseCase
import com.example.usecase.activity.DeleteActivityUseCase
import com.example.usecase.activity.FilterActivitiesUseCase
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
    var aantal = 0

    // post("/activities")
    get("/createActivity") {

        val activity: Activity = if (aantal == 0) {
            Activity(
                id = 2,
                activityName = "Varen langs de havens van Dordrecht",
                description = "Kom gezellig varen met vrienden langs de historische havens van Dordrecht. Inclusief muziek en snacks aan boord!",
                type = "Recreatie",
                location = "Dordrecht, Oude Haven",
                createdByUserId = 42,
                price = 12.50,
                imageUrl = "https://drijfdordrecht.nl/wp-content/uploads/2024/12/drijf-dordrecht-header-foto.jpg",
                isPublic = true,
                tags = listOf("varen", "boot", "gezellig", "dordrecht", "water")
            )
        } else {
            Activity(
                id = 4,
                activityName = "Historische stadswandeling Dordrecht",
                description = "Ontdek de rijke geschiedenis van Dordrecht...",
                type = "Cultuur",
                location = "Dordrecht, Stadscentrum",
                createdByUserId = 44,
                price = 10.00,
                imageUrl = null,
                isPublic = true,
                tags = listOf("wandeling", "cultuur")
            )
        }
        aantal++

        val result = createActivityUseCase.execute(activity)
        call.handle(result)
    }

    get("/activities") {
        val result = getActivitiesUseCase.execute()
        call.handle(result)
    }

    post("/activities/filter") {
        val filter = call.receive<ActivityFilterDto>()
        val result = filterActivitiesUseCase(filter)
        call.handle(result)
    }

    get ("/activities/{id}") {
        val id = call.parameters["id"]?.toIntOrNull()
        if (id == null){
            call.badRequest("Ongeldig of geen id")
            return@get
        }

        val result = getActivityUseCase.execute(id)
        call.handle(result)
    }

    delete ("/activities/{id}") {
        val id = call.parameters["id"]?.toIntOrNull()
        val result = deleteActivity.execute(id)
        call.handle(result)
    }
}