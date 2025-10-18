package com.example.usecase.activity

import com.example.core.ObjectResult
import model.Activity
import repository.ActivityRepository
import usecase.BaseUseCase

class GetFeaturedActivitiesUseCase(private val repository: ActivityRepository) : BaseUseCase<List<Activity>> {

    override suspend fun execute(): ObjectResult<List<Activity>> {
        val result = repository.getFeaturedActivities()
        return if (result.isNotEmpty()) {
            ObjectResult.success(result)
        } else {
            ObjectResult.notFound("Geen uitgelichte activiteiten gevonden")
        }
    }
}
