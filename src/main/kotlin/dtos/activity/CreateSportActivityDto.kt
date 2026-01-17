package dtos.activity

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
class CreateSportActivityDto(
    override val title: String,
    override val description: String,
    override val type: String = "",
    override val price: Double,
    override val locationLatitude: Double? = null,
    override val locationLongitude: Double? = null,
    override val locationId: Int? = null,
    override val capacity: Int? = null,
    override val startDate: LocalDate? = null,
    override val endDate: LocalDate? = null,
    override val recurrencePattern: String? = null,
    override val recurrenceDays: String? = null,
    override val phoneNumber: String? = null,
    val isIndoor: Boolean,
    val difficulty: String,
    val equipmentRequired: Boolean,
) : CreateActivityDto<CreateSportActivityDto>(
) {
}