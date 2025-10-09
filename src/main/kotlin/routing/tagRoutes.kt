package com.example.routing

import io.ktor.server.routing.*

fun Route.tagRoutes() {
    route("/tags") {

        get(){
            TODO()
        }
        get("/{id}"){
            TODO()
        }
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