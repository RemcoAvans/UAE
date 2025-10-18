package com.example.usecase.activity

import com.example.core.ObjectResult
import com.example.usecase.BaseInputUseCase
import model.Activity
import repository.ActivityRepository

class PromoteActivityUseCase(private val repository: ActivityRepository) : BaseInputUseCase<Int?, Activity> {

    override suspend fun execute(input: Int?): ObjectResult<Activity> {
        if (input == null || input < 0) {
            return ObjectResult.fail("Id is niet valide.")
        }

        val activity = repository.getById(input)
        if (activity == null) {
            return ObjectResult.notFound("Activiteit niet gevonden.")
        }

        if (activity.isFeatured) {
            return ObjectResult.fail("Activiteit is al uitgelicht.")
        }

        val promotedActivity = repository.setFeatured(input, true)
        return if (promotedActivity != null) {
            ObjectResult.success(promotedActivity)
        } else {
            ObjectResult.fail("Kon activiteit niet promoten.")
        }
    }
}
