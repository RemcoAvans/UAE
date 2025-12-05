package com.example.data

import com.example.data.models.*
import com.example.model.CultureActivity
import com.example.model.FoodActivity
import com.example.model.SportActivity
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init() {
        Database.connect(
            url = "jdbc:postgresql://localhost:5432/activities",
            driver = "org.postgresql.Driver",
            user = "postgres",
            password = "secret"
        )
        transaction {
            SchemaUtils.create(ActivityTable)
            SchemaUtils.create(ActivityTag)
            SchemaUtils.create(ActivityVote)
            SchemaUtils.create(SportActivity)
            SchemaUtils.create(FoodActivity)
            SchemaUtils.create(CultureActivity)
        }
    }
}
