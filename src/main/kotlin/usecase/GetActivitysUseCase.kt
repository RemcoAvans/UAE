package com.example.usecase

import com.example.core.ObjectResult
import com.example.repository.IActivityRepository
import model.Activity
import repository.ActivityRepository
import usecase.BaseUseCase

class GetActivitiesUseCase(private val repository : IActivityRepository) : BaseUseCase<List<Activity>> {

    override suspend fun execute(): ObjectResult<List<Activity>> {
        val result  = repository.getAll()
         return if (result.isNotEmpty()) {
             ObjectResult.success(result)
        } else {
             ObjectResult.notFound("Geen gebruikers gevonden")
        }
    }
}