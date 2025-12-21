package com.example.usecase.tag

import com.example.core.ObjectResult
import com.example.model.Tag
import com.example.usecase.BaseInputUseCase
import com.example.repository.IActivityTagRepository
import repository.CrudRepository

class DeleteTagUseCase(
    private val tagRepository: CrudRepository<Tag>,
    private val activityTagRepository: IActivityTagRepository
) : BaseInputUseCase<Int?, Boolean> {

    override suspend fun execute(input: Int?): ObjectResult<Boolean> {
        if (input == null || input < 0) {
            return ObjectResult.fail("Ongeldige tag ID")
        }

        // Check of tag bestaat
        val tag = tagRepository.getById(input)
        if (tag == null) {
            return ObjectResult.notFound("Tag niet gevonden")
        }

        // Verwijder eerst alle koppelingen met activities
        val activityTags = activityTagRepository.getByQuery { it.TagId == input }
        activityTags.forEach { activityTag ->
            activityTagRepository.delete(activityTag.id)
        }

        // Verwijder de tag
        val success = tagRepository.delete(input)
        return if (success) {
            ObjectResult.success(true)
        } else {
            ObjectResult.fail("Tag kon niet worden verwijderd")
        }
    }
}
