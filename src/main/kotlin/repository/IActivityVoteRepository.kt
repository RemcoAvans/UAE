package com.example.repository

import com.example.model.ActivityVote

interface IActivityVoteRepository {

    suspend fun getAll(): List<ActivityVote>

    suspend fun getById(id: Int): ActivityVote?

    suspend fun getByQuery(predicate: (ActivityVote) -> Boolean): List<ActivityVote>

    suspend fun create(entity: ActivityVote): ActivityVote

    suspend fun update(entity: ActivityVote): Boolean

    suspend fun delete(id: Int): Boolean
}