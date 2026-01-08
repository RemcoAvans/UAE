package com.example.repository

import com.example.model.Location
import dtos.activity.CreateActivityDto
import model.Activity
import repository.CrudRepository

interface ILocationRepository : CrudRepository<Location> {
}