package com.example.core

import io.ktor.http.HttpStatusCode

data class ObjectResult<T>(
    val result: T? = null,
    val success: Boolean,
    val message: String? = null,
    val statusCode: HttpStatusCode = HttpStatusCode.OK
) {
    companion object {
        fun <T> success(result: T): ObjectResult<T> {
            return ObjectResult(result = result, success = true)
        }

        fun <T> fail(message: String): ObjectResult<T> {
            return ObjectResult(result = null, success = false, message = message, statusCode = HttpStatusCode.BadRequest)
        }

        fun <T> notFound(message: String): ObjectResult<T> {
            return ObjectResult(result = null, success = false, message = message, statusCode = HttpStatusCode.NotFound)
        }
    }
}
