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
            TODO()
        }
        delete("/{id}"){
            TODO()
        }
        patch(){
            TODO()
        }
        post(){
            TODO()
        }
    }
}