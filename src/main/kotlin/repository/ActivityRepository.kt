package repository

import com.example.model.CultureActivity
import com.example.model.FoodActivity
import com.example.model.SportActivity
import model.Activity
import model.User

class ActivityRepository : CrudRepository<Activity> {

    private val activitys: MutableList<Activity> = mutableListOf() // een legen lijst om in memory data op te slaan
    private val sportActivities: MutableList<SportActivity> = mutableListOf() // een legen lijst om in memory data op te slaan
    private val foodActivities: MutableList<FoodActivity> = mutableListOf() // een legen lijst om in memory data op te slaan
    private val cultureActivities: MutableList<CultureActivity> = mutableListOf() // een legen lijst om in memory data op te slaan

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
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: Int): Boolean =
        activitys.removeIf { it.id == id }

    fun createSport(sportActivity: SportActivity) : SportActivity {
        sportActivities.add(sportActivity)
        return sportActivity
    }
    fun createFood(foodActivity: FoodActivity) : FoodActivity {
        foodActivities.add(foodActivity)
        return foodActivity
    }
    fun createCulture(cultureActivity: CultureActivity) : CultureActivity {
        cultureActivities.add(cultureActivity)
        return cultureActivity
    }
}