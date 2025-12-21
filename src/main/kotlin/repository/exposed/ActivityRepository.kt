package repository.exposed

import com.example.model.CultureActivity
import com.example.model.FoodActivity
import com.example.model.SportActivity
import com.example.repository.IActivityRepository
import kotlinx.datetime.LocalDate
import model.Activity
import kotlin.toString
import com.example.data.models.ActivityTable
import com.example.data.models.FoodActivityTable as FoodActivityTable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class ActivityRepository : IActivityRepository {

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    private fun toActivity(row: ResultRow): Activity =
        Activity(
            id = row[ActivityTable.id].value,
            title = row[ActivityTable.title],
            description = row[ActivityTable.description],
            photoUrl = row[ActivityTable.photoUrl],
            type = row[ActivityTable.type],
            price = row[ActivityTable.price],
            locationId = row[ActivityTable.locationId],
            capacity = row[ActivityTable.capacity],
            isFull = false,                   // <-- je hebt dit niet in DB, maar in model wel
            isFeatured = row[ActivityTable.isFeatured],
            startDate = LocalDate.parse(row[ActivityTable.startDate]),
            endDate = LocalDate.parse(row[ActivityTable.endDate]),
            createdAt = LocalDate.parse(row[ActivityTable.createdAt])
        )


    override suspend fun getAll(): List<Activity> = dbQuery {
        ActivityTable.selectAll().map(::toActivity)
    }

    override suspend fun getById(id: Int): Activity? =
        ActivityTable.selectAll().where { ActivityTable.id eq id }.toList()
            .map(::toActivity)
            .singleOrNull()

    override suspend fun getByQuery(predicate: (Activity) -> Boolean): List<Activity> =
        getAll().filter(predicate)

    override suspend fun create(entity: Activity): Activity = dbQuery {
        val id = ActivityTable.insertAndGetId { row ->
            row[title] = entity.title
            row[description] = entity.description
            row[photoUrl] = entity.photoUrl
            row[type] = entity.type
            row[price] = entity.price
            row[capacity] = entity.capacity
            row[locationId] = entity.locationId
            row[isFeatured] = entity.isFeatured
            row[startDate] = entity.startDate.toString()
            row[endDate] = entity.endDate.toString()
            row[createdAt] = entity.createdAt.toString()
            row[recurrencePattern] = entity.recurrencePattern ?: ""
            row[recurrenceDays] = entity.recurrenceDays ?: ""
            row[createdByUser] = 1 // placeholder foreign key
        }

        entity.copy(id = id.value)
    }

    override suspend fun update(id: Int, entity: Activity): Activity? = dbQuery {
        val updated = ActivityTable.update({ ActivityTable.id eq id }) { row ->
            row[title] = entity.title
            row[description] = entity.description
            row[photoUrl] = entity.photoUrl
            row[type] = entity.type
            row[price] = entity.price
            row[capacity] = entity.capacity
            row[locationId] = entity.locationId
            row[isFeatured] = entity.isFeatured
            row[startDate] = entity.startDate.toString()
            row[endDate] = entity.endDate.toString()
            row[createdAt] = entity.createdAt.toString()
            row[recurrenceDays] = entity.recurrenceDays ?: ""
            row[recurrencePattern] = entity.recurrencePattern ?: ""
        }

        if (updated > 0) getById(id) else null
    }

    override suspend fun delete(id: Int): Boolean = dbQuery {
        ActivityTable.deleteWhere { ActivityTable.id eq id } > 0
    }

    override fun setFeatured(id: Int, featured: Boolean): Activity? = transaction {
        ActivityTable.update({ ActivityTable.id eq id }) {
            it[isFeatured] = featured
        }

        ActivityTable.selectAll().where { ActivityTable.id eq id }
            .map(::toActivity)
            .singleOrNull()
    }

    override fun getFeaturedActivities(): List<Activity> = transaction {
        ActivityTable.selectAll().where { ActivityTable.isFeatured eq true }
            .map(::toActivity)
    }

    override fun updatePhotoUrl(id: Int, photoUrl: String) {
        transaction {
            ActivityTable.update({ ActivityTable.id eq id }) {
                it[ActivityTable.photoUrl] = photoUrl
            }
        }
    }

    // ------------------------------------------------------------
    // createSport / createFood / createCulture
    // Not fully implemented → skeletons
    // ------------------------------------------------------------
    override fun createSport(sportActivity: SportActivity): SportActivity {
        // Maak later een SportActivityTable
        return sportActivity
    }

    override fun createFood(foodActivity: FoodActivity): FoodActivity {
        transaction {
            FoodActivityTable.insert { row ->
                row[Cuisine] = foodActivity.Cuisine
                row[PriceRange] = foodActivity.PriceRange
            }
        }
        return foodActivity
    }

    override fun createCulture(cultureActivity: CultureActivity): CultureActivity {
        // Maak later een CultureActivityTable
        return cultureActivity
    }
}
