package routing

import com.example.baseRouter.BaseRouter.handle
import com.example.baseRouter.BaseRouter.unauthorized
import com.example.repository.ActivityTagRepository
import com.example.repository.TagRepository
import usecase.activity.CreateActivityUseCase
import com.example.usecase.GetActivitiesUseCase
import com.example.usecase.GetActivityUseCase
import com.example.usecase.activity.DeleteActivityUseCase
import com.example.usecase.activity.FilterActivitiesUseCase
import com.example.usecase.activity.GetActivityDetailsUseCase
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
import repository.ActivityVoteRepository

fun Route.activityRoutes(
    activityRepository: ActivityRepository,
    activityVoteRepository: ActivityVoteRepository,
    tagRepo: TagRepository,
    activityTagRepository: ActivityTagRepository
) {
    val createActivity = CreateActivityUseCase(activityRepository)
    val getActivities = GetActivitiesUseCase(activityRepository)
    val getActivityDetails = GetActivityDetailsUseCase(activityRepository, activityVoteRepository, activityTagRepository, tagRepo)
    val filterActivities = FilterActivitiesUseCase(activityRepository)
    val getActivity = GetActivityUseCase(activityRepository)
    val deleteActivity = DeleteActivityUseCase(activityRepository)
    val searchActivity = SearchActivityUseCase(filterActivities, activityRepository)

    route("/activities") {
        authenticate("auth-jwt") {
            get() {
                val result = getActivities.execute()
                call.handle(result)
            }


            post("/search") {
                val userInput = call.receiveText()
                val result = searchActivity.execute(userInput)
                call.handle(result)
            }

            post("/filter") {
                val filter = call.receive<ActivityFilterDto>()
                val result = filterActivities.execute(filter)
                call.handle(result)
            }

            get("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                val result = getActivity.execute(id)
                call.handle(result)
            }
        get("/Details/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val result = getActivityDetails.execute(id)
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
                val result = createActivity.execute(foodActivity);
                call.handle(result)
            }
            post("/culture") {
                val cultureActivity = call.receive<CreateCultureActivityDto>()
                val result = createActivity.execute(cultureActivity);
                call.handle(result)
            }
            post("/sport") {
                val sportActivity = call.receive<CreateSportActivityDto>()
                val result = createActivity.execute(sportActivity);
                call.handle(result)
            }
        }
    }
}
