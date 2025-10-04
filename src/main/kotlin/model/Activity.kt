package model

import kotlinx.serialization.Serializable

@Serializable
data class Activity (
    val id : Int,
    val activityName : String,
    val description : String,
    val type : String,
    val location : String,
    val createdByUserId : Int,
    val price : Double,
    val imageUrl: String? = null,
    val isPublic: Boolean = true,
    val tags: List<String?> = emptyList()


){}