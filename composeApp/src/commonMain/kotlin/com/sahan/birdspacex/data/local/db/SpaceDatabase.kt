package com.sahan.birdspacex.data.local.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.sahan.birdspacex.data.local.db.dao.LaunchDao
import com.sahan.birdspacex.data.local.db.dao.RocketDao
import com.sahan.birdspacex.data.local.db.entity.LaunchCacheEntity
import com.sahan.birdspacex.data.local.db.entity.RocketCacheEntity

@Database(
    entities = [
        LaunchCacheEntity::class,
        RocketCacheEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
@ConstructedBy(SpaceDatabaseConstructor::class)
abstract class SpaceDatabase : RoomDatabase() {
    abstract fun launchDao(): LaunchDao
    abstract fun rocketDao(): RocketDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object SpaceDatabaseConstructor : RoomDatabaseConstructor<SpaceDatabase> {
    override fun initialize(): SpaceDatabase
}
