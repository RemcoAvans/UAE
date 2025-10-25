package com.example.usecase.tag

import com.example.core.ObjectResult
import com.example.model.Tag
import com.example.usecase.BaseInputUseCase
import com.example.repository.TagRepository
import repository.CrudRepository

class UpdateTagUseCase(private val repository: CrudRepository<Tag>) : BaseInputUseCase<Tag, Tag> {

    override suspend fun execute(input: Tag): ObjectResult<Tag> {
        // Check of tag bestaat
        val existingTag = repository.getById(input.id)
        if (existingTag == null) {
            return ObjectResult.notFound("Tag niet gevonden")
        }

        // Check of nieuwe naam al bestaat bij een andere tag
        val duplicateTags = repository.getByQuery {
            it.name.equals(input.name, ignoreCase = true) && it.id != input.id
        }
        if (duplicateTags.isNotEmpty()) {
            return ObjectResult.fail("Tag met deze naam bestaat al")
        }

        val updatedTag = repository.update(input.id, input)
        return if (updatedTag != null) {
            ObjectResult.success(updatedTag)
        } else {
            ObjectResult.fail("Tag kon niet worden geüpdatet")
        }
    }
}
