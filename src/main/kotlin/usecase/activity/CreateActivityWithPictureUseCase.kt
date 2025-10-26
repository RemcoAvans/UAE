package com.example.usecase.activity

import com.example.Utilities.uploadPicture
import com.example.core.ObjectResult
import com.example.dtos.utilities.pictureDto
import com.example.dtos.utilities.splitMultipartDataAndPicturedDto
import com.example.repository.IActivityRepository
import com.example.usecase.BaseInputUseCase
import dtos.activity.CreateFoodActivityDto
import kotlinx.serialization.json.Json
import model.Activity
import usecase.activity.CreateActivityUseCase

class CreateActivityWithPictureUseCase(
    val createActivity: CreateActivityUseCase,
    val activityRepository: IActivityRepository
) : BaseInputUseCase<splitMultipartDataAndPicturedDto, Activity> {

    override suspend fun execute(input: splitMultipartDataAndPicturedDto): ObjectResult<Activity> {
        val errors = input.validate()
        if (errors.isNotEmpty()) {
            return ObjectResult.fail(errors.joinToString { ". " })
        }
        val foodActivity : CreateFoodActivityDto = Json.decodeFromString(input.jsonData!!)
        val activityResult = createActivity.execute(foodActivity)
        if (!activityResult.success || activityResult.result?.id == null) return activityResult

        val picture = pictureDto(input.originalFileName!!, input.fileBytes!!)
        val photoResult = uploadPicture(picture)
        if (photoResult.success) {
            activityRepository.updatePhotoUrl(activityResult.result.id, "/uploads/${photoResult.result}")
        }

        val activity = activityRepository.getById(activityResult.result.id)
        return ObjectResult.success(activity!!)
    }
}