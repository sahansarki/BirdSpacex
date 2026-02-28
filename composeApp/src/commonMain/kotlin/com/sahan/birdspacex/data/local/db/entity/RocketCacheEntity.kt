package com.sahan.birdspacex.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rocket_cache")
data class RocketCacheEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val timestamp: Long,
)
