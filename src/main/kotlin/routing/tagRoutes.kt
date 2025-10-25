package com.example.routing

import com.example.model.Tag
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import usecase.TagUseCase
import usecase.ActivityTagUseCase

fun Route.tagRoutes(
    tagUseCase: TagUseCase,
    activityTagUseCase: ActivityTagUseCase
) {
    route("/tags") {

        // get alle tags
        get {
            val tags = tagUseCase.getAllTags()
            call.respond(tags)
        }

        // get alle tags van één activity
        get("/{activityId}") {
            val activityId = call.parameters["activityId"]?.toIntOrNull()
            if (activityId == null) {
                call.respondText("Invalid activityId", status = HttpStatusCode.BadRequest)
                return@get
            }

            val tags = activityTagUseCase.getTagsForActivity(activityId)
            call.respond(tags)
        }

        // maak nieuwe tag
        post {
            val tag = call.receive<Tag>()
            val created = tagUseCase.createTag(tag.name)
            call.respond(created)
        }

        // wijzig tag
        patch {
            val tag = call.receive<Tag>()
            val updated = tagUseCase.updateTag(tag.id, tag.name)
            if (updated) call.respondText("Tag updated")
            else call.respondText("Tag not found", status = HttpStatusCode.NotFound)
        }

        // verwijder tag
        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respondText("Invalid id", status = HttpStatusCode.BadRequest)
                return@delete
            }

            val deleted = tagUseCase.deleteTag(id)
            if (deleted) call.respondText("Tag deleted")
            else call.respondText("Tag not found", status = HttpStatusCode.NotFound)
        }
    }
}
