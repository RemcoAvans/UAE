package repository

import com.example.model.ActivityTag
import com.example.model.Tag

class ActivityTagRepository {

    private val activityTags: MutableList<ActivityTag> = mutableListOf()

    suspend fun create(activityTag: ActivityTag): ActivityTag {
        activityTags.add(activityTag)
        return activityTag
    }

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

    suspend fun delete(activityId: Int, tagId: Int): Boolean {
        return activityTags.removeIf { it.ActivityId == activityId && it.TagId == tagId }
    }

    suspend fun deleteByActivityId(activityId: Int): Boolean {
        return activityTags.removeIf { it.ActivityId == activityId }
    }

    suspend fun deleteByTagId(tagId: Int): Boolean {
        return activityTags.removeIf { it.TagId == tagId }
    }
}
