package com.example

import TagRepository
import com.example.repository.ActivityTagRepository
import com.example.routing.analyticsDataRoutes
import com.example.routing.tagRoutes
import com.example.routing.voteRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import repository.ActivityRepository
import repository.ActivityVoteRepository
import repository.UserRepository
import routing.activityRoutes
import routing.userRoutes

//import routing.userRoutes

fun Application.configureRouting() {

    val activityRepository = ActivityRepository()
    val activityVoteRepository = ActivityVoteRepository()
    val activityTagRepository = ActivityTagRepository()
    val tagRepo = TagRepository()
    val userRepository = UserRepository()

    routing {
        userRoutes()
        activityRoutes(activityRepository, activityVoteRepository, tagRepo, activityTagRepository)
        voteRoutes(activityVoteRepository)
        analyticsDataRoutes()
        tagRoutes()
        get("/") {
            call.respondText("Hello World!")
        }
    }

}
