package repository

import model.Activity

class ActivityRepository : CrudRepository<Activity> {

    private val activitys: MutableList<Activity> = mutableListOf() // een legen lijst om in memory data op te slaan


    override suspend fun getAll(): List<Activity>  = activitys


    override suspend fun getById(id: Int): Activity? {
        return activitys.find { it.id == id }
    }

    override suspend fun create(entity: Activity): Activity {
        activitys.add(entity)
        return entity
    }

    override suspend fun update(id: Int, entity: Activity): Activity? {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: Int): Boolean {
        TODO("Not yet implemented")
    }

}