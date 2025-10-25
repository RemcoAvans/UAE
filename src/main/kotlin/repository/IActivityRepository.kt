package com.example.repository

import com.example.model.CultureActivity
import com.example.model.FoodActivity
import com.example.model.SportActivity
import model.Activity
import repository.CrudRepository

interface IActivityRepository : CrudRepository<Activity> {

    fun createSport(sportActivity: SportActivity) : SportActivity

    fun createFood(foodActivity: FoodActivity) : FoodActivity

    fun createCulture(cultureActivity: CultureActivity) : CultureActivity

    fun setFeatured(id: Int, featured: Boolean): Activity?

    fun getFeaturedActivities(): List<Activity>
}