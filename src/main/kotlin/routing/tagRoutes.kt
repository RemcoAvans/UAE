package com.example.routing

import io.ktor.server.routing.*

fun Route.tagRoutes() {
    route("/tags") {

        get(){
            TODO("Get alle tags")
        }
        get("/{activityId}"){
            TODO("Get all tags bij één activity")
        }
        delete("/{id}"){
            TODO("een simpele delete tag")
        }
        patch(){
            TODO("een simpele update tag")
        }
        post(){
            TODO("een simpele create tag")
        }
        // to do: een endpoint dat een tag koppelt aan een activity (en dus een ActivityTag entitieit aanmaakt)
    }
}