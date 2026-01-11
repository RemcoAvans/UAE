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
import repository.CrudRepository

class GetActivitiesByLocation(
    private val activityRepo: IActivityRepository,
    private val locationRepo: ILocationRepository,
    private val voteRepo: IActivityVoteRepository,
    private val activityTagRepo: IActivityTagRepository,
    private val tagRepo: CrudRepository<Tag>
) : BaseInputUseCase<Location, List<ActivityDetailDto>> {

    override suspend fun execute(input: Location): ObjectResult<List<ActivityDetailDto>> {

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

        val result = activities.mapNotNull { activity ->
            val location = locationMap[activity.locationId] ?: return@mapNotNull null

            ActivityDetailDto(
                id = activity.id,
                title = activity.title,
                description = activity.description,
                photoUrl = activity.photoUrl,
                type = activity.type,
                price = activity.price,
                createdByUserId = activity.createdByUserId,
                locationId = activity.locationId,
                latitude = location.latitude,
                longitude = location.longitude,
                isHighlighted = activity.isFeatured,
                capacity = activity.capacity,
                isFull = activity.isFull,
                startDate = activity.startDate,
                endDate = activity.endDate,
                recurrencePattern = activity.recurrencePattern,
                recurrenceDays = activity.recurrenceDays,
                createdAt = activity.createdAt,
                rating = getRating(activity.id),
                tags = getTags(activity.id)
            )
        }

        return ObjectResult.success(result)
    }

    private suspend fun getRating(activityId: Int): Int {
        val votes = voteRepo.getByQuery { it.activityId == activityId }
        return votes.count { it.positive } - votes.count { !it.positive }
    }

    private suspend fun getTags(activityId: Int): List<String> {
        val activityTags = activityTagRepo.getByQuery { it.ActivityId == activityId }
        return activityTags.mapNotNull {
            tagRepo.getById(it.TagId)?.name
        }
    }
}
