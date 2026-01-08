package com.example

import com.example.repository.exposed.*
//import com.example.repository.memory.ActivityTagRepository
//import com.example.repository.memory.TagRepository
//import repository.ActivityRepository
//import repository.ActivityVoteRepository
//import repository.AnalyticsDataRepository
//import repository.UserRepository
import com.example.routing.analyticsDataRoutes
import com.example.routing.tagRoutes
import com.example.routing.voteRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import repository.exposed.ActivityRepository
import repository.exposed.ActivityVoteRepository
import repository.exposed.AnalyticsDataRepository
import repository.exposed.UserRepository
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
    val locationRepository = LocationRepository()

    routing {
        userRoutes(userRepo)
        activityRoutes(activityRepo, activityVoteRepo, tagRepo, activityTagRepo, locationRepository)
        voteRoutes(activityVoteRepo)
        analyticsDataRoutes(analyticsDataRepo, activityRepo)
        tagRoutes(tagRepo, activityTagRepo, activityRepo)
        get("/") {
            call.respondText("Hello World!")
        }
    }
}
