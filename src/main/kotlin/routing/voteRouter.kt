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
            TODO("delete de vote")
        }
        post(){
            TODO("Nieuwe vote. overschrijft de vorige vote van de user(id) met activityId")
        }
    }
}