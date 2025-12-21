package com.example.routing

import com.example.baseRouter.BaseRouter.badRequest
import com.example.baseRouter.BaseRouter.handle
import com.example.model.Tag
import com.example.usecase.tag.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import com.example.repository.IActivityRepository
import com.example.repository.IActivityTagRepository
import repository.CrudRepository

fun Route.tagRoutes(
    tagRepo: CrudRepository<Tag>,
    activityTagRepo: IActivityTagRepository,
    activityRepo: IActivityRepository
) {
    val getAllTagsUseCase = GetAllTagsUseCase(tagRepo)
    val getTagsByActivityUseCase = GetTagsByActivityUseCase(tagRepo, activityTagRepo)
    val createTagUseCase = CreateTagUseCase(tagRepo)
    val updateTagUseCase = UpdateTagUseCase(tagRepo)
    val deleteTagUseCase = DeleteTagUseCase(tagRepo, activityTagRepo)
    val linkTagToActivityUseCase = LinkTagToActivityUseCase(activityRepo, tagRepo, activityTagRepo)

    route("/tags") {
        // GET /tags - Alle tags ophalen
        get {
            val result = getAllTagsUseCase.execute()
            call.handle(result)
        }

        // GET /tags/{activityId} - Alle tags van een specifieke activity
        get("/{activityId}") {
            val activityId = call.parameters["activityId"]?.toIntOrNull()
            if (activityId == null) {
                call.badRequest("Ongeldige of geen activity ID")
                return@get
            }
            val result = getTagsByActivityUseCase.execute(activityId)
            call.handle(result)
        }

        // POST /tags - Nieuwe tag aanmaken
        post {
            val tag = call.receive<Tag>()
            val result = createTagUseCase.execute(tag)
            call.handle(result)
        }

        // PATCH /tags - Tag updaten
        patch {
            val tag = call.receive<Tag>()
            val result = updateTagUseCase.execute(tag)
            call.handle(result)
        }

        // DELETE /tags/{id} - Tag verwijderen
        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.badRequest("Ongeldige of geen tag ID")
                return@delete
            }
            val result = deleteTagUseCase.execute(id)
            call.handle(result)
        }

        // POST /tags/link - Tag koppelen aan activity
        post("/link") {
            val input = call.receive<LinkTagInput>()
            val result = linkTagToActivityUseCase.execute(input)
            call.handle(result)
        }
    }
}