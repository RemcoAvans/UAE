package com.example.routing

import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.route


fun Route.voteRoutes() {
    route("/votes") {
        get("/{activityId}"){
            TODO("Get alle votes van de activities")
        }
        delete("/{id}"){
            val id = call.parameters["id"]?.toIntOrNull()

            if (id == null || id <= 0) {
                call.respond(ObjectResult.fail("voteID is geen geldig getal"))
                return@delete
            }
            val result = DeleteVoteUseCase.execute(id)

            call.respond(result)
        }

        post(){
            TODO("Nieuwe vote. overschrijft de vorige vote van de user(id) met activityId")
        }
    }
}