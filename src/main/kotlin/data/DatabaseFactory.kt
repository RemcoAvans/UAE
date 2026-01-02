package com.example.data

import com.example.data.models.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init() {
        Database.connect(
            url = "jdbc:postgresql://aws-1-eu-west-1.pooler.supabase.com:6543/postgres?sslmode=require",
            driver = "org.postgresql.Driver",
            user = "postgres.locvhwyvbjplqhhuwrag",
            password = "2igiM0PhqIU53QZO", // 2igiM0PhqIU53QZO
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
