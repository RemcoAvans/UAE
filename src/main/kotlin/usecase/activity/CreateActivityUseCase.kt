package usecase.activity

import com.example.core.ObjectResult
import com.example.model.CultureActivity
import com.example.model.FoodActivity
import com.example.model.SportActivity
import com.example.repository.IActivityRepository
import com.example.usecase.BaseInputUseCase
import dtos.activity.CreateActivityDto
import dtos.activity.CreateCultureActivityDto
import dtos.activity.CreateFoodActivityDto
import dtos.activity.CreateSportActivityDto
import model.Activity
import repository.ActivityRepository

class CreateActivityUseCase(private val repository : IActivityRepository) :
    BaseInputUseCase<CreateActivityDto<*>, Activity> {
    override suspend fun execute(input: CreateActivityDto<*>): ObjectResult<Activity> {
        // Later: haal userId uit auth principal
        val userId = 0
        val activity = input.toActivity(userId)
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
}