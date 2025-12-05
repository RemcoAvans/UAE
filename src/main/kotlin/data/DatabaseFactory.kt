package com.example.data

import com.example.data.models.*
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
            SchemaUtils.create(AnalyticsData)
            SchemaUtils.create(CultureActivity)
            SchemaUtils.create(FoodActivity)
            SchemaUtils.create(Location)
            SchemaUtils.create(SearchLog)
            SchemaUtils.create(SportActivity)
            SchemaUtils.create(Tag)
            SchemaUtils.create(User)
        }
    }
}
