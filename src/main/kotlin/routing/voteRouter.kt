package com.example.routing

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import usecase.vote.CreateOrUpdateVoteUseCase
import usecase.vote.DeleteVoteUseCase
import usecase.vote.GetVotesByActivityUseCase
import repository.ActivityVoteRepository
import dtos.CreateVoteDto

fun Route.voteRoutes() {
    val repository = ActivityVoteRepository()
    val deleteVoteUseCase = DeleteVoteUseCase(repository)
    val getVotesByActivityUseCase = GetVotesByActivityUseCase(repository)
    val createOrUpdateVoteUseCase = CreateOrUpdateVoteUseCase(repository)

    route("/votes") {
        get("/{activityId}") {
            val activityId = call.parameters["activityId"]?.toIntOrNull()
            val result = getVotesByActivityUseCase.execute(activityId)
            call.respond(result)
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val result = deleteVoteUseCase.execute(id)
            call.respond(result)
        }

        post {
            val dto = call.receive<CreateVoteDto>()
            val result = createOrUpdateVoteUseCase.execute(dto)
            call.respond(result)
        }
    }
}