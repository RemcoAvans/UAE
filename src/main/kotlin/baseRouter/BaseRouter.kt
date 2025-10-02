package com.example.baseRouter

import com.example.core.ObjectResult
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import usecase.GetUsersUseCase

object BaseRouter {

    suspend fun <T> ApplicationCall.handle(data: ObjectResult<T>) {
        if (data.success) {
            respond(status = data.statusCode, message = data.result ?: "")
        } else {
            respond(status = data.statusCode, message = mapOf("error" to (data.message ?: "Er is een fout opgetreden")))
        }
    }

    suspend fun ApplicationCall.ok(data: Any) {
        respond(HttpStatusCode.OK, data)
    }

    suspend fun ApplicationCall.notFound(message: String = "Not found") {
        respond(HttpStatusCode.NotFound, mapOf("error" to message))
    }

    suspend fun ApplicationCall.badRequest(message: String = "Bad request") {
        respond(HttpStatusCode.BadRequest, mapOf("error" to message))
    }
}