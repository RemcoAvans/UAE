package com.example

import com.example.repository.memory.ActivityTagRepository
import com.example.repository.memory.TagRepository
import com.example.routing.analyticsDataRoutes
import com.example.routing.tagRoutes
import com.example.routing.voteRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import repository.ActivityRepository
import repository.ActivityVoteRepository
import repository.AnalyticsDataRepository
import repository.UserRepository
import routing.activityRoutes
import routing.userRoutes

//import routing.userRoutes

fun Application.configureRouting() {

    val activityRepo = ActivityRepository()
    val activityVoteRepo = ActivityVoteRepository()
    val activityTagRepo = ActivityTagRepository()
    val tagRepo = TagRepository()
    val userRepo = UserRepository()
    val analyticsDataRepo = AnalyticsDataRepository()

    routing {
        userRoutes(userRepo)
        activityRoutes(activityRepo, activityVoteRepo, tagRepo, activityTagRepo)
        voteRoutes(activityVoteRepo)
        analyticsDataRoutes(analyticsDataRepo, activityRepo)
        tagRoutes(tagRepo, activityTagRepo, activityRepo)
        get("/") {
            call.respondText("Hello World!")
        }
    }
}
