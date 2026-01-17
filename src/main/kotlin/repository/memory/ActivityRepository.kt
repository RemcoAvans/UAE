package repository

import com.example.model.CultureActivity
import com.example.model.FoodActivity
import com.example.model.SportActivity
import com.example.repository.IActivityRepository
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import model.Activity

open class ActivityRepository : IActivityRepository {

    private val activities: MutableList<Activity> = mutableListOf() // een legen lijst om in memory data op te slaan
    private val sportActivities: MutableList<SportActivity> = mutableListOf() // een legen lijst om in memory data op te slaan
    private val foodActivities: MutableList<FoodActivity> = mutableListOf() // een legen lijst om in memory data op te slaan
    private val cultureActivities: MutableList<CultureActivity> = mutableListOf() // een legen lijst om in memory data op te slaan

    init {
        createMockData()
    }

    override suspend fun getAll(): List<Activity>  = activities

    override suspend fun getById(id: Int): Activity? {
        return activities.find { it.id == id }
    }

    override suspend fun getByQuery(predicate: (Activity) -> Boolean): List<Activity> {
        return activities.filter(predicate)
    }

    override suspend fun create(entity: Activity): Activity {
        val nextId = (activities.maxOfOrNull { it.id } ?: 0) + 1
        val today: LocalDate = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
        val newActivity = entity.copy(
            id = nextId,
            createdAt = today
        )
        activities.add(newActivity)
        return newActivity
    }

    override suspend fun update(id: Int, entity: Activity): Activity? {
        val index = activities.indexOfFirst { it.id == id }
        if (index != -1) {
            activities[index] = entity
            return entity
        }
        return null
    }

    override suspend fun delete(id: Int): Boolean =
        activities.removeIf { it.id == id }

    open override fun setFeatured(id: Int, featured: Boolean): Activity? {
        val activity = activities.find { it.id == id } ?: return null
        val updatedActivity = activity.copy(isFeatured = featured)
        val index = activities.indexOfFirst { it.id == id }
        if (index != -1) {
            activities[index] = updatedActivity
            return updatedActivity
        }
        return null
    }

    open override fun getFeaturedActivities(): List<Activity> {
        return activities.filter { it.isFeatured }
    }

    override fun updatePhotoUrl(id: Int, photoUrl: String) {
        val index = activities.indexOfFirst { it.id == id }
        if (index != -1) {
            activities[index] = activities[index].copy(photoUrl = photoUrl)
        }
    }

    override fun createSport(sportActivity: SportActivity) : SportActivity {
        sportActivities.add(sportActivity)
        return sportActivity
    }

    override fun createFood(foodActivity: FoodActivity) : FoodActivity {
        foodActivities.add(foodActivity)
        return foodActivity
    }

    override fun createCulture(cultureActivity: CultureActivity) : CultureActivity {
        cultureActivities.add(cultureActivity)
        return cultureActivity
    }

    private fun createMockData() {
        // Mock Sport Activities
        sportActivities.addAll(
            listOf(
                SportActivity(1, true, "Beginner", true),
                SportActivity(2, false, "Intermediate", false),
                SportActivity(3, true, "Advanced", true)
            )
        )

        // Mock Food Activities
        foodActivities.addAll(
            listOf(
                FoodActivity(4, "Italian", "€€"),
                FoodActivity(5, "Japanese", "€€€"),
                FoodActivity(6, "Mexican", "€")
            )
        )

        // Mock Culture Activities
        cultureActivities.addAll(
            listOf(
                CultureActivity(7, "Theater", "Dutch", 12),
                CultureActivity(8, "Museum", "English", 0),
                CultureActivity(9, "Concert", "Dutch", 16),
                CultureActivity(10, "Exhibition", "French", 0)
            )
        )

        val today = LocalDate.parse("2025-10-01")

        // Combine all types into generic Activity list
        activities.addAll(
            listOf(
                Activity(
                    id = 1,
                    title = "Indoor Climbing",
                    description = "Climb walls in a safe indoor environment.",
                    photoUrl = "https://example.com/climbing.jpg",
                    type = "Sport",
                    price = 25.0,
                    locationId = 101,
                    capacity = 10,
                    isFull = false,
                    startDate = today,
                    endDate = today,
                    phoneNumber = "0612345678"
                ),
                Activity(
                    id = 2,
                    title = "Running in the Park",
                    description = "Join a group run through the city park.",
                    photoUrl = "https://example.com/running.jpg",
                    type = "Sport",
                    price = 0.0,
                    locationId = 102,
                    capacity = 30,
                    isFull = false,
                    startDate = today,
                    endDate = today,
                    phoneNumber = "0612345678"
                ),
                Activity(
                    id = 3,
                    title = "Yoga Class",
                    description = "Relax and stretch during an indoor yoga session.",
                    photoUrl = "https://example.com/yoga.jpg",
                    type = "Sport",
                    price = 15.0,
                    locationId = 103,
                    capacity = 12,
                    isFull = false,
                    startDate = today,
                    endDate = today,
                    phoneNumber = "0612345678"
                ),
                Activity(
                    id = 4,
                    title = "Italian Cooking Workshop",
                    description = "Learn to cook authentic Italian pasta dishes.",
                    photoUrl = "https://example.com/pasta.jpg",
                    type = "Food",
                    price = 40.0,
                    locationId = 104,
                    capacity = 8,
                    isFull = false,
                    startDate = today,
                    endDate = today,
                    phoneNumber = "0612345678"
                ),
                Activity(
                    id = 5,
                    title = "Sushi Making Evening",
                    description = "Master the art of sushi rolling with an expert chef.",
                    photoUrl = "https://example.com/sushi.jpg",
                    type = "Food",
                    price = 50.0,
                    locationId = 105,
                    capacity = 10,
                    isFull = false,
                    startDate = today,
                    endDate = today,
                    phoneNumber = "0612345678"
                ),
                Activity(
                    id = 6,
                    title = "Taco Tuesday",
                    description = "Enjoy a Mexican food tasting night.",
                    photoUrl = "https://example.com/tacos.jpg",
                    type = "Food",
                    price = 20.0,
                    locationId = 106,
                    capacity = 20,
                    isFull = false,
                    startDate = today,
                    endDate = today,
                    phoneNumber = "0612345678"
                ),
                Activity(
                    id = 7,
                    title = "Local Theater Night",
                    description = "A Dutch comedy play at the local stage.",
                    photoUrl = "https://example.com/theater.jpg",
                    type = "Culture",
                    price = 30.0,
                    locationId = 107,
                    capacity = 50,
                    isFull = false,
                    startDate = today,
                    endDate = today,
                    phoneNumber = "0612345678"
                ),
                Activity(
                    id = 8,
                    title = "Art Museum Tour",
                    description = "Guided tour through modern art collections.",
                    photoUrl = "https://example.com/museum.jpg",
                    type = "Culture",
                    price = 18.0,
                    locationId = 108,
                    capacity = 20,
                    isFull = false,
                    startDate = today,
                    endDate = today,
                    phoneNumber = "0612345678"
                ),
                Activity(
                    id = 9,
                    title = "Rock Concert",
                    description = "Live performance by a local rock band.",
                    photoUrl = "https://example.com/concert.jpg",
                    type = "Culture",
                    price = 35.0,
                    locationId = 109,
                    capacity = 100,
                    isFull = false,
                    startDate = today,
                    endDate = today,
                    phoneNumber = "0612345678"
                ),
                Activity(
                    id = 10,
                    title = "French Art Exhibition",
                    description = "Experience new works from French painters.",
                    photoUrl = "https://example.com/exhibition.jpg",
                    type = "Culture",
                    price = 12.0,
                    locationId = 110,
                    capacity = 40,
                    isFull = false,
                    startDate = today,
                    endDate = today,
                    phoneNumber = "0612345678"
                ),
            )
        )
    }
}