package com.example.usecase

import com.example.core.ObjectResult
import model.Activity
import repository.ActivityRepository

class CreateActivityUseCase(private val repository : ActivityRepository ) : BaseInputUseCase <Activity, Activity> {
    override suspend fun execute(input: Activity): ObjectResult<Activity> {

        val result : Activity = repository.create(input)

        return ObjectResult.success(result)
    }
}