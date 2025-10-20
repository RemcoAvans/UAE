package routing
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import dtos.CreateVoteDto
import repository.ActivityVoteRepository
import usecase.vote.GetVotesByActivityUseCase
import usecase.vote.DeleteVoteUseCase
import usecase.vote.CreateOrUpdateVoteUseCase

fun Route.voteRoutes() {
    route("/votes") {
        get("/{activityId}") {
            val activityId = call.parameters["activityId"]?.toIntOrNull()

            val repo = ActivityVoteRepository()
            val getVotesUseCase = GetVotesByActivityUseCase(repo)
            val result = getVotesUseCase.execute(activityId)

            call.respond(result)
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()

            val repo = ActivityVoteRepository()
            val deleteVoteUseCase = DeleteVoteUseCase(repo)
            val result = deleteVoteUseCase.execute(id)

            call.respond(result)
        }

        post {
            val voteDto = call.receive<CreateVoteDto>()

            val repo = ActivityVoteRepository()
            val createOrUpdateVoteUseCase = CreateOrUpdateVoteUseCase(repo)
            val result = createOrUpdateVoteUseCase.execute(voteDto)

            call.respond(result)
        }
    }
}
