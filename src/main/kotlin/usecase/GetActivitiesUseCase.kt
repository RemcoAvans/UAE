package com.example.usecase

import com.example.core.ObjectResult
import com.example.repository.IActivityRepository
import model.Activity
import usecase.BaseUseCase

class GetActivitiesUseCase(private val repository : IActivityRepository) : BaseUseCase<List<Activity>> {

    override suspend fun execute(): ObjectResult<List<Activity>> {
        val result  = repository.getAll()
        result.forEach { activity ->
            if (!activity.photoUrl.isNullOrEmpty()) {
                activity.photoUrl = "http://10.0.2.2:8080/uploads/${activity.photoUrl}"
                }
        }

        return ObjectResult.success(result)
    }
}