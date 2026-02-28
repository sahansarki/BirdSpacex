package com.sahan.birdspacex.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sahan.birdspacex.data.local.db.entity.LaunchCacheEntity

@Dao
interface LaunchDao {

    @Query("SELECT * FROM launch_cache")
    suspend fun getAll(): List<LaunchCacheEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<LaunchCacheEntity>)

    @Query("DELETE FROM launch_cache")
    suspend fun clear()

    @Query("SELECT MAX(timestamp) FROM launch_cache")
    suspend fun latestTimestamp(): Long?
}
