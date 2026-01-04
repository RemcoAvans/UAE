package usecase.activity

import com.example.core.ObjectResult
import com.example.model.CultureActivity
import com.example.model.FoodActivity
import com.example.model.Location
import com.example.model.SportActivity
import com.example.repository.IActivityRepository
import com.example.repository.ILocationRepository
import com.example.usecase.BaseInputUseCase
import dtos.activity.CreateActivityDto
import dtos.activity.CreateCultureActivityDto
import dtos.activity.CreateFoodActivityDto
import dtos.activity.CreateSportActivityDto
import model.Activity
import repository.ActivityRepository

class CreateActivityUseCase(
    private val repository : IActivityRepository,
    private val locationRepository : ILocationRepository) :
    BaseInputUseCase<CreateActivityDto<*>, Activity> {
    override suspend fun execute(input: CreateActivityDto<*>): ObjectResult<Activity> {
        val userId = 0
        val locationId = getLocation(input.locationLatitude, input.locationLongitude)
        val activity = input.toActivity(userId, locationId)
        val result = repository.create(activity)
        when (input) {
            is CreateSportActivityDto -> {
                repository.createSport(
                    SportActivity(
                        id = activity.id,
                        isIndoor = input.isIndoor,
                        difficulty = input.difficulty,
                        equipmentRequired = input.equipmentRequired
                    )
                )
            }
            is CreateFoodActivityDto -> {
                repository.createFood(
                    FoodActivity(
                        Id = activity.id,
                        Cuisine = input.Cuisine,
                        PriceRange = input.PriceRange
                    )
                )
            }
            is CreateCultureActivityDto -> {
                repository.createCulture(
                    CultureActivity(
                        id = activity.id,
                        genre = input.genre,
                        language = input.language,
                        ageRestriction = input.ageRestriction
                    )
                )
            }
        }
        return ObjectResult.success(result)
    }

    private suspend fun getLocation(locationLatitude: Double, locationLongitude: Double): Int {
        val existingLocation = locationRepository.getByQuery { it.latitude == locationLatitude && it.longitude == locationLongitude }
        if (existingLocation.isNotEmpty())
            return existingLocation[0].id

        val location = Location(
            0,
            locationLatitude,
            locationLongitude,
            "",
            "",
        )
        val result = locationRepository.create(location)
        return result.id
    }
}