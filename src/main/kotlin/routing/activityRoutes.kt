package routing

import com.example.baseRouter.BaseRouter.badRequest
import com.example.baseRouter.BaseRouter.handle
import com.example.core.ObjectResult
import com.example.usecase.CreateActivityUseCase
import com.example.usecase.GetActivityUseCase
import com.example.usecase.GetActivitysUseCase
import io.ktor.server.routing.*
import model.Activity

import repository.ActivityRepository


fun Route.activityRoutes() {
    val repo = ActivityRepository()
    val createActivityUseCase = CreateActivityUseCase(repo)
    val getActivitysUseCase = GetActivitysUseCase(repo)
    val getActivityUseCase = GetActivityUseCase(repo)
    var aantal = 0

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
        val result : ObjectResult<List<Activity>> = getActivitysUseCase.execute()
        call.handle(result)

    }

    get ("/activitie/{id}") {
        val id = call.parameters["id"]?.toIntOrNull()
        if (id == null){
            call.badRequest("Ongeldig of geen id")
            return@get
        }

        val result : ObjectResult<Activity?> = getActivityUseCase.execute(id)
        call.handle(result)

    }
}