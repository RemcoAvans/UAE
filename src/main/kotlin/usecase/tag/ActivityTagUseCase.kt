package usecase

import com.example.model.Tag
import repository.ActivityTagRepository
import repository.TagRepository

class ActivityTagUseCase(
    private val activityTagRepository: ActivityTagRepository,
    private val tagRepository: TagRepository
) {
    fun getTagsForActivity(activityId: Int): List<Tag> {
        val tagIds = activityTagRepository.getTagsForActivity(activityId)
        return tagRepository.getAll().filter { it.id in tagIds }
    }
}
