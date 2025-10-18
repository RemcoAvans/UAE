package repository

import model.Activity
import model.User

class ActivityRepository : CrudRepository<Activity> {

    private val activitys: MutableList<Activity> = mutableListOf() // een legen lijst om in memory data op te slaan


    override suspend fun getAll(): List<Activity>  = activitys


    override suspend fun getById(id: Int): Activity? {
        return activitys.find { it.id == id }
    }

    override suspend fun getByQuery(predicate: (Activity) -> Boolean): List<Activity> {
        return activitys.filter(predicate)
    }

    override suspend fun create(entity: Activity): Activity {
        activitys.add(entity)
        return entity
    }

    override suspend fun update(id: Int, entity: Activity): Activity? {
        val index = activitys.indexOfFirst { it.id == id }
        return if (index != -1) {
            activitys[index] = entity
            entity
        } else {
            null
        }
    }

    override suspend fun delete(id: Int): Boolean =
        activitys.removeIf { it.id == id }

    suspend fun setFeatured(id: Int, isFeatured: Boolean): Activity? {
        val activity = getById(id) ?: return null
        val updatedActivity = activity.copy(isFeatured = isFeatured)
        return update(id, updatedActivity)
    }

    suspend fun getFeaturedActivities(): List<Activity> {
        return activitys.filter { it.isFeatured }
    }
}