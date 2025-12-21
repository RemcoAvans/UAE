package com.example.usecase.tag

import com.example.core.ObjectResult
import com.example.model.ActivityTag
import com.example.model.Tag
import com.example.usecase.BaseInputUseCase
import com.example.repository.IActivityRepository
import com.example.repository.IActivityTagRepository
import repository.CrudRepository

data class LinkTagInput(
    val activityId: Int,
    val tagId: Int
)

class LinkTagToActivityUseCase(
    private val activityRepository: IActivityRepository,
    private val tagRepository: CrudRepository<Tag>,
    private val activityTagRepository: IActivityTagRepository
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
        val existingTagIds = activityTagRepository.getByQuery { it.ActivityId == input.activityId }.map { it.TagId }
        if (existingTagIds.contains(input.tagId)) {
            return ObjectResult.fail("Deze tag is al gekoppeld aan deze activity")
        }

        // Maak de koppeling
        val nextId = activityTagRepository.getAll().maxOfOrNull { it.id }?.plus(1) ?: 1
        val activityTag = ActivityTag(
            id = nextId,
            ActivityId = input.activityId,
            TagId = input.tagId
        )
        val createdLink = activityTagRepository.create(activityTag)
        return ObjectResult.success(createdLink)
    }
}
