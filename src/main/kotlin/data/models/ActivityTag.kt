package com.example.data.models

import org.jetbrains.exposed.dao.id.IntIdTable

object ActivityTag : IntIdTable("activityTags"){
    val ActivityId = integer("ActivityId")
    val TagId = integer("TagId")
}
