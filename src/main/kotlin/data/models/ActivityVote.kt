package com.example.data.models

import com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date
import org.jetbrains.exposed.dao.id.IntIdTable

object ActivityVote : IntIdTable("activityVotes") {
    val activityId = integer("activityId")
    val userId = integer("userId")
    val createAt = date("createAt")
    val positive = bool("positive")
}
