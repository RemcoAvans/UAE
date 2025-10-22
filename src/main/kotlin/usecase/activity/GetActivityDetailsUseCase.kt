package com.example.usecase.activity

import TagRepository
import com.example.core.ObjectResult
import com.example.dtos.activity.ActivityDetailDto
import com.example.repository.ActivityTagRepository
import com.example.usecase.BaseInputUseCase
import repository.ActivityRepository
import repository.ActivityVoteRepository

class GetActivityDetailsUseCase(
    private val activityRepo: ActivityRepository,
    private val voteRepo: ActivityVoteRepository,
    private val activityTagRepository: ActivityTagRepository,
    private val tagRepo: TagRepository
) : BaseInputUseCase<Int?, ActivityDetailDto> {

    override suspend fun execute(input: Int?): ObjectResult<ActivityDetailDto> {
        if (input == null || input < 1) {
            return ObjectResult.fail("Id is niet valide.")
        }
        val activity = activityRepo.getById(input)
            ?: return ObjectResult.notFound("Activiteit niet gevonden.")

        val ratings = getRating(input)
        val tags = getTags(input)

        val result = ActivityDetailDto(
            id = activity.id,
            title = activity.title,
            description = activity.description,
            photoUrl = activity.photoUrl,
            type = activity.type,
            price = activity.price,
            createdByUserId = activity.createdByUserId,
            locationId = activity.locationId,
            isHighlighted = activity.isHighlighted,
            capacity = activity.capacity,
            isFull = activity.isFull,
            startDate = activity.startDate,
            endDate = activity.endDate,
            recurrencePattern = activity.recurrencePattern,
            recurrenceDays = activity.recurrenceDays,
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
}
