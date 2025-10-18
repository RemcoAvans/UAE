package com.example.usecase.tag

import com.example.core.ObjectResult
import com.example.model.ActivityTag
import com.example.usecase.BaseInputUseCase
import repository.ActivityRepository
import repository.ActivityTagRepository
import repository.TagRepository

data class LinkTagInput(
    val activityId: Int,
    val tagId: Int
)

class LinkTagToActivityUseCase(
    private val activityRepository: ActivityRepository,
    private val tagRepository: TagRepository,
    private val activityTagRepository: ActivityTagRepository
) : BaseInputUseCase<LinkTagInput, ActivityTag> {

    override suspend fun execute(input: LinkTagInput): ObjectResult<ActivityTag> {
        // Validatie: check of activity bestaat
        val activity = activityRepository.getById(input.activityId)
        if (activity == null) {
            return ObjectResult.notFound("Activity niet gevonden")
        }

        // Validatie: check of tag bestaat
        val tag = tagRepository.getById(input.tagId)
        if (tag == null) {
            return ObjectResult.notFound("Tag niet gevonden")
        }

        // Check of koppeling al bestaat
        val existingTagIds = activityTagRepository.getTagsByActivityId(input.activityId)
        if (existingTagIds.contains(input.tagId)) {
            return ObjectResult.fail("Deze tag is al gekoppeld aan deze activity")
        }

        // Maak de koppeling
        val activityTag = ActivityTag(
            ActivityId = input.activityId,
            TagId = input.tagId
        )
        val createdLink = activityTagRepository.create(activityTag)
        return ObjectResult.success(createdLink)
    }
}
