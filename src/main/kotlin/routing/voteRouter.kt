package com.example.routing

import com.example.baseRouter.BaseRouter.handle
import com.example.repository.IActivityVoteRepository
import io.ktor.server.request.*
import io.ktor.server.routing.*
import usecase.vote.CreateOrUpdateVoteUseCase
import usecase.vote.DeleteVoteUseCase
import usecase.vote.GetVotesByActivityUseCase
import repository.ActivityVoteRepository
import dtos.vote.CreateVoteDto

fun Route.voteRoutes(repository: IActivityVoteRepository) {
    val deleteVoteUseCase = DeleteVoteUseCase(repository)
    val getVotesByActivityUseCase = GetVotesByActivityUseCase(repository)
    val createOrUpdateVoteUseCase = CreateOrUpdateVoteUseCase(repository)

    route("/votes") {
        get("/{activityId}") {
            val activityId = call.parameters["activityId"]?.toIntOrNull()
            val result = getVotesByActivityUseCase.execute(activityId)
            call.handle(result)
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val result = deleteVoteUseCase.execute(id)
            call.handle(result)
        }

        post {
            val dto = call.receive<CreateVoteDto>()
            val result = createOrUpdateVoteUseCase.execute(dto)
            call.handle(result)
        }
    }
}