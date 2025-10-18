package com.example.usecase.tag

import com.example.core.ObjectResult
import com.example.model.Tag
import com.example.usecase.BaseInputUseCase
import repository.ActivityTagRepository
import repository.TagRepository

class GetTagsByActivityUseCase(
    private val tagRepository: TagRepository,
    private val activityTagRepository: ActivityTagRepository
) : BaseInputUseCase<Int?, List<Tag>> {

    override suspend fun execute(input: Int?): ObjectResult<List<Tag>> {
        if (input == null || input < 0) {
            return ObjectResult.fail("Ongeldige activity ID")
        }

        val tagIds = activityTagRepository.getTagsByActivityId(input)
        if (tagIds.isEmpty()) {
            return ObjectResult.notFound("Geen tags gevonden voor deze activity")
        }

        val tags = tagIds.mapNotNull { tagId ->
            tagRepository.getById(tagId)
        }

        return ObjectResult.success(tags)
    }
}
