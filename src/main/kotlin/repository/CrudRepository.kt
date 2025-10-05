package repository

import model.User

interface CrudRepository<TEntity> {

    suspend fun getAll(): List<TEntity>

    suspend fun getById(id: Int): TEntity?

    suspend fun getByQuery(predicate: (TEntity) -> Boolean): List<TEntity>

    suspend fun create(entity: TEntity): TEntity

    suspend fun update(id: Int, entity: TEntity): TEntity?

    suspend fun delete(id: Int): Boolean
}