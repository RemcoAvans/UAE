package com.example

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import routing.userRoutes

fun Application.configureRouting() {
    routing {
        userRoutes()
        get("/") {
            call.respondText("Hello World!")
        }
    }
}
