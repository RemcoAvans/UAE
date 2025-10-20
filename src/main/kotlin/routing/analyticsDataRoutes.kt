package com.example.routing

import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route


fun Route.analyticsDataRoutes() {
    route("/analyticsData") {
        get(){
            TODO("get alle analyticsData ")
        }
        get("/{activityId}"){
            TODO("get alle analyticsData die horen bij een specifieke activity (via location)")
        }
    }
}