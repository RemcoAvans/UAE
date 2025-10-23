package com.example.core

import ai.koog.agents.core.tools.annotations.LLMDescription
import ai.koog.agents.core.tools.annotations.Tool
import ai.koog.agents.core.tools.reflect.ToolSet
import com.example.repository.IActivityRepository
import com.example.usecase.activity.FilterActivitiesUseCase
import dtos.activity.ActivityFilterDto
import model.Activity
import repository.ActivityRepository

class FilterActivitiesTool(
    private val filterUseCase: FilterActivitiesUseCase,
    private val repo: IActivityRepository
) : ToolSet {
    @Tool
    @LLMDescription("fetch all activities")
    suspend fun getAllActivities(): List<Activity> {
        val result = repo.getAll()
        return result
    }

    @Tool
    @LLMDescription("fetch all Activity types/categories")
    suspend fun getAllTypes(): String {
        return "categories: Culture, Food, Sport"
    }

    @Tool
    @LLMDescription("fetch the activities that satisfy the given filter")
    suspend fun searchActivities(params: ActivityFilterDto): List<Activity> {
        val result = filterUseCase.execute(params)
        return result.result ?: emptyList()
    }
}
