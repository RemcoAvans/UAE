package com.example.repository

import com.example.model.ActivityTag
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

    // Specific methods for activity-tag relationships
    suspend fun getTagsByActivityId(activityId: Int): List<Int> {
        return activityTags
            .filter { it.ActivityId == activityId }
            .map { it.TagId }
    }

    suspend fun getActivitiesByTagId(tagId: Int): List<Int> {
        return activityTags
            .filter { it.TagId == tagId }
            .map { it.ActivityId }
    }

    suspend fun deleteByActivityId(activityId: Int): Boolean {
        return activityTags.removeIf { it.ActivityId == activityId }
    }

    suspend fun deleteByTagId(tagId: Int): Boolean {
        return activityTags.removeIf { it.TagId == tagId }
    }
}
