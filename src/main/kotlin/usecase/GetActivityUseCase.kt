package com.example.usecase

import com.example.core.ObjectResult
import io.ktor.server.routing.RoutingFailureStatusCode
import model.Activity
import repository.ActivityRepository
import usecase.BaseUseCase
import kotlin.collections.isNotEmpty

class GetActivityUseCase(private val repository : ActivityRepository) : BaseInputUseCase<Int, Activity?>  {
    override suspend fun execute(id : Int): ObjectResult<Activity?> {
        val result = repository.getById(id)
        return if (result != null) {
            ObjectResult.success(result)
        } else {
            ObjectResult.notFound("Geen gebruikers gevonden")
        }

    }


}