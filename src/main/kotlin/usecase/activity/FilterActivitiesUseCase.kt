package com.example.usecase.activity

import com.example.Dtos.ActivityFilterDto
import com.example.core.ObjectResult
import com.example.usecase.BaseInputUseCase
import model.Activity
import repository.ActivityRepository

class FilterActivitiesUseCase(val repository: ActivityRepository) : BaseInputUseCase<ActivityFilterDto, List<Activity>> {

    override suspend fun execute(input: ActivityFilterDto): ObjectResult<List<Activity>> {
        val activityList = repository.getAll()
        if (activityList.isEmpty()) {
            return ObjectResult.notFound("Geen activiteiten gevonden.")
        }
        val filtered = activityList
            .asSequence() // maakt filteren efficiënter bij veel data
            .filter { activity ->
                // Price filters
                (input.minPrice == null || activity.price >= input.minPrice) &&
                        (input.maxPrice == null || activity.price <= input.maxPrice)
            }
            .filter { activity ->
                // City filter
                input.city == null || activity.location.equals(input.city, ignoreCase = true)
            }
            .filter { activity ->
                // Category filter (lijst)
                input.categories.isNullOrEmpty() || input.categories.contains(activity.type)
            }
//            .filter { activity ->
//                // Date filters (optioneel)
//                val dateFromOk = input.dateFrom == null || activity.date >= input.dateFrom
//                val dateToOk = input.dateTo == null || activity.date <= input.dateTo
//                dateFromOk && dateToOk
//            }
            .toList()
        return ObjectResult.success(filtered)
    }
}