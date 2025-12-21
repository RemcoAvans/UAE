package com.example.usecase.tag

import com.example.core.ObjectResult
import com.example.model.Tag
import com.example.usecase.BaseInputUseCase
import repository.CrudRepository

class CreateTagUseCase(private val repository: CrudRepository<Tag>) : BaseInputUseCase<Tag, Tag> {

    override suspend fun execute(input: Tag): ObjectResult<Tag> {
        // Validatie: check of tag naam al bestaat
        val existingTag = repository.getByQuery { it.name.equals(input.name, ignoreCase = true) }
        if (existingTag.isNotEmpty()) {
            return ObjectResult.fail("Tag met deze naam bestaat al")
        }

        val createdTag = repository.create(input)
        return ObjectResult.success(createdTag)
    }
}
