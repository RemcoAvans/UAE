package com.example.data

import com.example.data.models.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init() {
        Database.connect(
            url = "jdbc:postgresql://db.locvhwyvbjplqhhuwrag.supabase.co:5432/postgres?sslmode=require",
            driver = "org.postgresql.Driver",
            user = "postgres",
            password = $$"cu3eZeq8BuDR?!$e3h$3", // cu3eZeq8BuDR?!$e3h$3
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
