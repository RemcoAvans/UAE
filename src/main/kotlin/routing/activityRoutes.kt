package routing

import com.example.baseRouter.BaseRouter.badRequest
import com.example.baseRouter.BaseRouter.handle
import com.example.baseRouter.BaseRouter.unauthorized
import usecase.activity.CreateActivityUseCase
import com.example.usecase.GetActivitiesUseCase
import com.example.usecase.GetActivityUseCase
import com.example.usecase.activity.DeleteActivityUseCase
import com.example.usecase.activity.FilterActivitiesUseCase
import com.example.usecase.activity.SearchActivityUseCase
import dtos.activity.ActivityFilterDto
import dtos.activity.CreateCultureActivityDto
import dtos.activity.CreateFoodActivityDto
import dtos.activity.CreateSportActivityDto
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.request.receiveText
import io.ktor.server.routing.*

import repository.ActivityRepository

fun Route.activityRoutes() {
    val repo = ActivityRepository()
    val createActivityUseCase = CreateActivityUseCase(repo)
    val getActivitiesUseCase = GetActivitiesUseCase(repo)
    val filterActivitiesUseCase = FilterActivitiesUseCase(repo)
    val getActivityUseCase = GetActivityUseCase(repo)
    val deleteActivity = DeleteActivityUseCase(repo)
    val searchActivityUseCase = SearchActivityUseCase(filterActivitiesUseCase, repo)

    route("/activities") {
        authenticate("auth-jwt") {
            get() {
                val result = getActivitiesUseCase.execute()
                call.handle(result)
            }


            post("/search") {
                val userInput = call.receiveText()
                val result = searchActivityUseCase.execute(userInput)
                call.handle(result)
            }

            post("/filter") {
                val filter = call.receive<ActivityFilterDto>()
                val result = filterActivitiesUseCase.execute(filter)
                call.handle(result)
            }

            get("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                val result = getActivityUseCase.execute(id)
                call.handle(result)
            }
        get("/Details/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val result = getActivityUseCase.execute(id)
            call.handle(result)
        }

            delete("/{id}") {
                val principal = call.principal<JWTPrincipal>()
                val role = principal?.payload?.getClaim("role")?.asString()
                if (role != "Admin"){
                    call.unauthorized("Unauthenticated user")
                    return@delete
                }
                val id = call.parameters["id"]?.toIntOrNull()
                val result = deleteActivity.execute(id)
                call.handle(result)
            }

            post("/food") {
                val foodActivity = call.receive<CreateFoodActivityDto>()
                val result = createActivityUseCase.execute(foodActivity);
                call.handle(result)
            }
            post("/culture") {
                val cultureActivity = call.receive<CreateCultureActivityDto>()
                val result = createActivityUseCase.execute(cultureActivity);
                call.handle(result)
            }
            post("/sport") {
                val sportActivity = call.receive<CreateSportActivityDto>()
                val result = createActivityUseCase.execute(sportActivity);
                call.handle(result)
            }
        }
    }
}
