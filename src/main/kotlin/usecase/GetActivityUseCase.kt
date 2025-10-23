package com.example.usecase

import com.example.core.ObjectResult
import com.example.repository.IActivityRepository
import io.ktor.server.routing.RoutingFailureStatusCode
import model.Activity
import repository.ActivityRepository
import usecase.BaseUseCase
import kotlin.collections.isNotEmpty

class GetActivityUseCase(private val repository : IActivityRepository) : BaseInputUseCase<Int?, Activity?>  {
    override suspend fun execute(input : Int?): ObjectResult<Activity?> {
        if (input == null || input < 0) {
            return ObjectResult.fail("Id is niet validen.")
        }
        val result = repository.getById(input)
        return if (result != null) {
            ObjectResult.success(result)
        } else {
            ObjectResult.notFound("No Activity found")
        }

    }


}