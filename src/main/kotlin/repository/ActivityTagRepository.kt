package com.example.repository

import com.example.model.ActivityTag
import model.User
import repository.CrudRepository

open class ActivityTagRepository : CrudRepository<ActivityTag> {

    private val activityTags = mutableListOf<ActivityTag>()

    override suspend fun getAll(): List<ActivityTag> = activityTags

    override suspend fun getById(id: Int): ActivityTag? {
        return activityTags.find { it.id == id }
    }

    override suspend fun getByQuery(predicate: (ActivityTag) -> Boolean): List<ActivityTag> {
        return activityTags.filter(predicate)
    }

    override suspend fun create(entity: ActivityTag): ActivityTag {
        activityTags.add(entity)
        return entity
    }

    override suspend fun update(id: Int, entity: ActivityTag): ActivityTag? {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: Int): Boolean =
        activityTags.removeIf { it.id == id }

}