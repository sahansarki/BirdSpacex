package com.sahan.birdspacex.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sahan.birdspacex.data.local.db.entity.RocketCacheEntity

@Dao
interface RocketDao {

    @Query("SELECT * FROM rocket_cache")
    suspend fun getAll(): List<RocketCacheEntity>

    @Query("SELECT * FROM rocket_cache WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): RocketCacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: RocketCacheEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<RocketCacheEntity>)

    @Query("DELETE FROM rocket_cache")
    suspend fun clear()

    @Query("SELECT timestamp FROM rocket_cache WHERE id = :id LIMIT 1")
    suspend fun timestampById(id: String): Long?
}