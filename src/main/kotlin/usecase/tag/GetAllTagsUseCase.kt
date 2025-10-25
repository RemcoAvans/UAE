package com.example.usecase.tag

import com.example.core.ObjectResult
import com.example.model.Tag
import com.example.repository.TagRepository
import repository.CrudRepository
import usecase.BaseUseCase

class GetAllTagsUseCase(private val repository: CrudRepository<Tag>) : BaseUseCase<List<Tag>> {

    override suspend fun execute(): ObjectResult<List<Tag>> {
        val tags = repository.getAll()
        return if (tags.isNotEmpty()) {
            ObjectResult.success(tags)
        } else {
            ObjectResult.notFound("Geen tags gevonden")
        }
    }
}
