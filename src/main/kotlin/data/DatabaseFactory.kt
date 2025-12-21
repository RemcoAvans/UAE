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
            SchemaUtils.create(ActivityTagTable)
            SchemaUtils.create(ActivityVoteTable)
            SchemaUtils.create(AnalyticsDataTable)
            SchemaUtils.create(CultureActivityTable)
            SchemaUtils.create(FoodActivityTable)
            SchemaUtils.create(LocationTable)
            SchemaUtils.create(SearchLogTable)
            SchemaUtils.create(SportActivityTable)
            SchemaUtils.create(TagTable)
            SchemaUtils.create(UserTable)
        }
    }
}
