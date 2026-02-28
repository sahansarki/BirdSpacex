package com.sahan.birdspacex.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "launch_cache")
data class LaunchCacheEntity(
    @PrimaryKey val id: String,
    val missionName: String,
    val dateUtc: String,
    val rocketId: String,
    val success: Boolean?,
    val patchImageUrl: String?,
    val timestamp: Long,
)