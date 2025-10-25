package repository

import com.example.model.ActivityTag

class ActivityTagRepository {

    private val activityTags = mutableListOf<ActivityTag>()

    fun getTagsForActivity(activityId: Int): List<Int> =
        activityTags.filter { it.ActivityId == activityId }.map { it.TagId }

    fun linkTagToActivity(activityTag: ActivityTag) {
        // prevent duplicate link
        if (!activityTags.any { it.ActivityId == activityTag.ActivityId && it.TagId == activityTag.TagId }) {
            activityTags.add(activityTag)
        }
    }

    fun unlinkTagFromActivity(activityId: Int, tagId: Int): Boolean =
        activityTags.removeIf { it.ActivityId == activityId && it.TagId == tagId }
}
