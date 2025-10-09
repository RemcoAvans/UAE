package com.example

import com.example.routing.analyticsDataRoutes
import com.example.routing.tagRoutes
import com.example.routing.voteRoutes
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import routing.activityRoutes
//import routing.userRoutes

fun Application.configureRouting() {
    routing {
//        userRoutes()
        activityRoutes()
        voteRoutes()
        analyticsDataRoutes()
        tagRoutes()
        get("/") {
            call.respondText("Hello World!")
        }
    }
}
