package com.example.usecase.activity

import com.example.core.ObjectResult
import com.example.dtos.activity.ActivityDetailDto
import com.example.model.Tag
import com.example.repository.IActivityTagRepository
import com.example.repository.IActivityVoteRepository
import com.example.repository.ILocationRepository
import com.example.usecase.BaseInputUseCase
import model.Activity
import repository.CrudRepository

class GetActivityDetailListUseCase(
    private val locationRepo: ILocationRepository,
    private val voteRepo: IActivityVoteRepository,
    private val activityTagRepo: IActivityTagRepository,
    private val tagRepo: CrudRepository<Tag>
) : BaseInputUseCase<List<Activity>?, List<ActivityDetailDto>> {

    override suspend fun execute(input: List<Activity>?): ObjectResult<List<ActivityDetailDto>> {

        if (input == null || input.isEmpty()) {
            return ObjectResult.success(emptyList())
        }

        // ---- Locations in 1 keer ophalen
        val locationIds = input.map { it.locationId }.toSet()

        val locations = locationRepo.getByQuery {
            it.id in locationIds
        }.associateBy { it.id }

        // ---- Votes in 1 keer ophalen
        val activityIds = input.map { it.id }.toSet()

        val votes = voteRepo.getByQuery {
            it.activityId in activityIds
        }.groupBy { it.activityId }

        // ---- Tags in 1 keer ophalen
        val activityTags = activityTagRepo.getByQuery {
            it.ActivityId in activityIds
        }.groupBy { it.ActivityId }

        val tagIds = activityTags.values
            .flatten()
            .map { it.TagId }
            .toSet()

        val tags = tagRepo.getByQuery {
            it.id in tagIds
        }.associateBy { it.id }

        // ---- DTO mapping
        val result = input.map { activity ->
            val location = locations[activity.locationId]

            val rating = votes[activity.id]
                ?.let { v -> v.count { it.positive } - v.count { !it.positive } }
                ?: 0

            val tagNames = activityTags[activity.id]
                ?.mapNotNull { tags[it.TagId]?.name }
                ?: emptyList()
            val defaultLatitude = 52.1326
            val defaultLongitude = 5.2913
            ActivityDetailDto(
                id = activity.id,
                title = activity.title,
                description = activity.description,
                photoUrl = activity.photoUrl,
                type = activity.type,
                price = activity.price,
                createdByUserId = activity.createdByUserId,
                locationId = activity.locationId,
                latitude = location?.latitude ?: defaultLatitude,
                longitude = location?.longitude ?: defaultLongitude,
                isHighlighted = activity.isFeatured,
                capacity = activity.capacity,
                isFull = activity.isFull,
                startDate = activity.startDate,
                endDate = activity.endDate,
                recurrencePattern = activity.recurrencePattern,
                recurrenceDays = activity.recurrenceDays,
                createdAt = activity.createdAt,
                rating = rating,
                tags = tagNames
            )
        }

        return ObjectResult.success(result)
    }

}