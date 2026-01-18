package routing

import com.example.Utilities.splitMultipartDataAndPicture
import com.example.baseRouter.BaseRouter.handle
import com.example.baseRouter.BaseRouter.unauthorized
import com.example.model.Location
import com.example.model.Tag
import com.example.repository.IActivityRepository
import com.example.repository.IActivityTagRepository
import com.example.repository.IActivityVoteRepository
import com.example.repository.ILocationRepository
import usecase.activity.CreateActivityUseCase
import com.example.usecase.GetActivitiesUseCase
import com.example.usecase.GetActivityUseCase
import com.example.usecase.activity.CreateActivityWithPictureUseCase
import com.example.usecase.activity.DeleteActivityUseCase
import com.example.usecase.activity.FilterActivitiesUseCase
import com.example.usecase.activity.GetActivitiesByLocation
import com.example.usecase.activity.GetActivityDetailListUseCase
import com.example.usecase.activity.GetActivityDetailsUseCase
import com.example.usecase.activity.SearchActivityUseCase
import com.example.usecase.activity.getPhotoUseCase
import dtos.activity.ActivityFilterDto
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.request.receiveMultipart
import io.ktor.server.request.receiveText
import io.ktor.server.response.*
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.routing.*

import repository.CrudRepository

fun Route.activityRoutes(
    activityRepository: IActivityRepository,
    activityVoteRepository: IActivityVoteRepository,
    tagRepo: CrudRepository<Tag>,
    activityTagRepository: IActivityTagRepository,
    locationRepository: ILocationRepository,
) {
    val createActivity = CreateActivityUseCase(activityRepository, locationRepository)
    val getActivities = GetActivitiesUseCase(activityRepository)
    val getActivityDetails = GetActivityDetailsUseCase(activityRepository, activityVoteRepository, activityTagRepository, tagRepo, locationRepository)
    val filterActivities = FilterActivitiesUseCase(activityRepository)
    val getActivity = GetActivityUseCase(activityRepository)
    val deleteActivity = DeleteActivityUseCase(activityRepository)
    val searchActivity = SearchActivityUseCase(filterActivities, activityRepository)
    val createActivityWithPicture = CreateActivityWithPictureUseCase(createActivity, activityRepository)
    val getActivitiesByLocation = GetActivitiesByLocation(
        activityRepository,
        locationRepository,
        activityVoteRepository,
        activityTagRepository,
        tagRepo)
    val getActivityDetailList = GetActivityDetailListUseCase(
        locationRepository,
        activityVoteRepository,
        activityTagRepository,
        tagRepo)
    val getPhoto = getPhotoUseCase()

    route("/activities") {
        get() { // Let op voor testen van ophalen fotos heb ik hem buiten de Auth gezet moet rterug als dit nog niet gedaan is !
            val activities = getActivities.execute()
            val result = getActivityDetailList.execute(activities.result)
            call.handle(result)
        }
        get("{lat}/{lon}") {
            val lat = call.parameters["lat"]!!.toDouble()
            val lon = call.parameters["lon"]!!.toDouble()
            val location = Location(0, lat,lon,"","")
            val activities = getActivitiesByLocation.execute(location)
            val result = getActivityDetailList.execute(activities.result)
            call.handle(result)
        }
        get("/photo/{filename}") {
            val filename = call.parameters["filename"] ?: return@get call.respond(HttpStatusCode.BadRequest, "Bestandsnaam ontbreekt")
            try {
                val result: ByteArray = getPhoto.getPicture(filename)
                
                if (result.isEmpty()) {
                    call.respond(HttpStatusCode.NotFound, "Foto niet gevonden")
                    return@get
                }

                val contentType = when (filename.substringAfterLast('.', "").lowercase()) {
                    "jpg", "jpeg" -> ContentType.Image.JPEG
                    "png" -> ContentType.Image.PNG
                    "gif" -> ContentType.Image.GIF
                    else -> ContentType.Application.OctetStream
                }

                call.respondBytes(
                    bytes = result,
                    contentType = contentType,
                    status = HttpStatusCode.OK
                )
            } catch (e: Exception) {
                call.respond(HttpStatusCode.NotFound, "Foto niet gevonden")
            }
        }
        authenticate("auth-jwt") {

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
                val multipartData = call.receiveMultipart(formFieldLimit = 1024 * 1024 * 100)
                val data = splitMultipartDataAndPicture(multipartData)
                data.type = "food"
                var result = createActivityWithPicture.execute(data)
                call.handle(result)
            }

            post("/culture") {
                val multipartData = call.receiveMultipart(formFieldLimit = 1024 * 1024 * 100)
                val data = splitMultipartDataAndPicture(multipartData)
                data.type = "culture"
                val result = createActivityWithPicture.execute(data)
                call.handle(result)
            }
            post("/sport") {
                val multipartData = call.receiveMultipart(formFieldLimit = 1024 * 1024 * 100)
                val data = splitMultipartDataAndPicture(multipartData)
                data.type = "sport"
                val result = createActivityWithPicture.execute(data)
                call.handle(result)
            }
        }
    }
}
