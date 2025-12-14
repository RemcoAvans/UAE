package com.example.data.models

import org.jetbrains.exposed.dao.id.IntIdTable

object ActivityVoteTable : IntIdTable("activityVotes") {
    val activityId = integer("activityId")
    val userId = integer("userId")
    val createAt = varchar("createAt", 255)
    val positive = bool("positive")
}
