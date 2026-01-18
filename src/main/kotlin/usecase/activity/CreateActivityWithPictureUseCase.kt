package com.example.usecase.activity

import com.example.Utilities.uploadPicture
import com.example.core.ObjectResult
import com.example.dtos.utilities.pictureDto
import com.example.dtos.utilities.splitMultipartDataAndPicturedDto
import com.example.repository.IActivityRepository
import com.example.usecase.BaseInputUseCase
import dtos.activity.CreateActivityDto
import dtos.activity.CreateCultureActivityDto
import dtos.activity.CreateFoodActivityDto
import dtos.activity.CreateSportActivityDto
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
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
        val json = Json { ignoreUnknownKeys = false }
        val jsonElement = json.parseToJsonElement(input.jsonData!!)
        val type = jsonElement.jsonObject["type"]?.jsonPrimitive?.content?.lowercase() 
            ?:input.type
            ?: return ObjectResult.fail("Type veld ontbreekt in JSON")
        
        val activityDto: CreateActivityDto<*> = when (type) {
            "food" -> json.decodeFromString<CreateFoodActivityDto>(input.jsonData!!)
            "culture" -> json.decodeFromString<CreateCultureActivityDto>(input.jsonData!!)
            "sport" -> json.decodeFromString<CreateSportActivityDto>(input.jsonData!!)
            else -> return ObjectResult.fail("Onbekend activiteitstype: $type")
        }

        when (type) {
            "food" -> activityDto.type = "food"
            "culture" -> activityDto.type = "culture"
            "sport" -> activityDto.type = "sport"
            else -> return ObjectResult.fail("Onbekend activiteitstype: $type")
        }
        
        val activityResult = createActivity.execute(activityDto)
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