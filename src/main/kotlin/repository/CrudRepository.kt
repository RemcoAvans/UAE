package repository

interface CrudRepository<TEntity> {

    suspend fun getAll(): List<TEntity>

    suspend fun getById(id: Int): TEntity?

    suspend fun create(entity: TEntity): TEntity

    suspend fun update(id: Int, entity: TEntity): TEntity?

    suspend fun delete(id: Int): Boolean
}