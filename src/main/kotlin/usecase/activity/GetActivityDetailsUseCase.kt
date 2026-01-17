package com.example.usecase.activity

import com.example.core.ObjectResult
import com.example.dtos.activity.ActivityDetailDto
import com.example.model.Location
import com.example.model.Tag
import com.example.repository.IActivityRepository
import com.example.repository.IActivityTagRepository
import com.example.repository.IActivityVoteRepository
import com.example.repository.ILocationRepository
import com.example.repository.exposed.LocationRepository
import com.example.usecase.BaseInputUseCase
import repository.CrudRepository

class GetActivityDetailsUseCase(
    private val activityRepo: IActivityRepository,
    private val voteRepo: IActivityVoteRepository,
    private val activityTagRepository: IActivityTagRepository,
    private val tagRepo: CrudRepository<Tag>,
    private val locationRepository: ILocationRepository,
) : BaseInputUseCase<Int?, ActivityDetailDto> {

    override suspend fun execute(input: Int?): ObjectResult<ActivityDetailDto> {
        if (input == null || input < 1) {
            return ObjectResult.fail("Id is niet valide.")
        }
        val activity = activityRepo.getById(input)
            ?: return ObjectResult.notFound("Activiteit niet gevonden.")

        val ratings = getRating(input)
        val tags = getTags(input)
        val location = getLocation(activity.locationId)

        val result = ActivityDetailDto(
            id = activity.id,
            title = activity.title,
            description = activity.description,
            photoUrl = activity.photoUrl,
            type = activity.type,
            price = activity.price,
            createdByUserId = activity.createdByUserId,
            locationId = activity.locationId,
            latitude = location?.latitude ?: 0.0,
            longitude = location?.longitude ?: 0.0,
            isHighlighted = activity.isFeatured,
            capacity = activity.capacity,
            isFull = activity.isFull,
            startDate = activity.startDate,
            endDate = activity.endDate,
            recurrencePattern = activity.recurrencePattern,
            recurrenceDays = activity.recurrenceDays,
            phoneNumber = activity.phoneNumber,
            createdAt = activity.createdAt,
            rating = ratings,
            tags = tags,
        )
        return ObjectResult.success(result)
    }

    private suspend fun getRating(activityId: Int): Int {
        val votes = voteRepo.getByQuery { it.activityId == activityId }
        return votes.filter{it.positive}.size - votes.filter{!it.positive}.size;
    }

    private suspend fun getTags(activityId: Int): List<String> {
        val activityTags = activityTagRepository.getByQuery { it.ActivityId == activityId }
        val tagNames = mutableListOf<String>()
        for (activityTag in activityTags) {
            val tag = tagRepo.getById(activityTag.TagId)
            if (tag != null) {
                tagNames.add(tag.name)
            }
        }
        return tagNames
    }

    private suspend fun getLocation(locationId: Int) : Location? {
        val location = locationRepository.getById(locationId)
        return location
    }
}
