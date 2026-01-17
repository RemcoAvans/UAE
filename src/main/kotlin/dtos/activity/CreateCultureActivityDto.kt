package dtos.activity

import io.ktor.client.plugins.logging.EMPTY
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
class CreateCultureActivityDto(
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
    val genre: String,
    val language: String,
    val ageRestriction: Int,
) : CreateActivityDto<CreateCultureActivityDto>(
    ) {
}