package repository

import com.example.model.AnalyticsData

open class AnalyticsDataRepository : CrudRepository<AnalyticsData> {

    private val analyticsData: MutableList<AnalyticsData> = mutableListOf()

    override suspend fun getAll(): List<AnalyticsData> = analyticsData

    override suspend fun getById(id: Int): AnalyticsData? {
        return analyticsData.find { it.id == id }
    }

    override suspend fun getByQuery(predicate: (AnalyticsData) -> Boolean): List<AnalyticsData> {
        return analyticsData.filter(predicate)
    }

    override suspend fun create(entity: AnalyticsData): AnalyticsData {
        analyticsData.add(entity)
        return entity
    }

    override suspend fun update(id: Int, entity: AnalyticsData): AnalyticsData? {
        val index = analyticsData.indexOfFirst { it.id == id }
        return if (index != -1) {
            analyticsData[index] = entity
            entity
        } else {
            null
        }
    }

    override suspend fun delete(id: Int): Boolean {
        return analyticsData.removeIf { it.id == id }
    }

    open suspend fun getByLocationId(locationId: Int): AnalyticsData? {
        return analyticsData.find { it.locationId == locationId }
    }
}
