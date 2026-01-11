package com.example.usecase.activity

import com.example.core.ObjectResult
import com.example.dtos.activity.ActivityDetailDto
import com.example.model.Location
import com.example.model.Tag
import com.example.repository.IActivityRepository
import com.example.repository.IActivityTagRepository
import com.example.repository.IActivityVoteRepository
import com.example.repository.ILocationRepository
import com.example.services.CalculateBoundingBoxService.calculateBoundingBox
import com.example.usecase.BaseInputUseCase
import model.Activity
import repository.CrudRepository

class GetActivitiesByLocation(
    private val activityRepo: IActivityRepository,
    private val locationRepo: ILocationRepository,
    private val voteRepo: IActivityVoteRepository,
    private val activityTagRepo: IActivityTagRepository,
    private val tagRepo: CrudRepository<Tag>
) : BaseInputUseCase<Location, List<Activity>> {

    override suspend fun execute(input: Location): ObjectResult<List<Activity>> {

        val radiusMeters = 5000.0 // 5 km

        val box = calculateBoundingBox(
            latitude = input.latitude,
            longitude = input.longitude,
            radiusInMeters = radiusMeters
        )

        val nearbyLocations = locationRepo.getByQuery {
            it.latitude  >= box.minLat &&
            it.latitude  <= box.maxLat &&
            it.longitude >= box.minLon &&
            it.longitude <= box.maxLon
        }

        if (nearbyLocations.isEmpty()) {
            return ObjectResult.success(emptyList())
        }

        val locationMap = nearbyLocations.associateBy { it.id }

        val activities = activityRepo.getByQuery {
            it.locationId in locationMap.keys
        }

        return ObjectResult.success(activities)
    }
}
