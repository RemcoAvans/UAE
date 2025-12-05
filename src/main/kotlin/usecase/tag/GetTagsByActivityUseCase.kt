package com.example.usecase.tag

import com.example.core.ObjectResult
import com.example.model.Tag
import com.example.usecase.BaseInputUseCase
import com.example.repository.IActivityTagRepository
import repository.CrudRepository

class GetTagsByActivityUseCase(
    private val tagRepository: CrudRepository<Tag>,
    private val activityTagRepository: IActivityTagRepository
) : BaseInputUseCase<Int?, List<Tag>> {

    override suspend fun execute(input: Int?): ObjectResult<List<Tag>> {
        if (input == null || input < 0) {
            return ObjectResult.fail("Ongeldige activity ID")
        }

        val tagIds = activityTagRepository.getByQuery { it.ActivityId == input }.map { it.TagId }
        if (tagIds.isEmpty()) {
            return ObjectResult.notFound("Geen tags gevonden voor deze activity")
        }

        val tags = tagIds.mapNotNull { tagId ->
            tagRepository.getById(tagId)
        }

        return ObjectResult.success(tags)
    }
}
