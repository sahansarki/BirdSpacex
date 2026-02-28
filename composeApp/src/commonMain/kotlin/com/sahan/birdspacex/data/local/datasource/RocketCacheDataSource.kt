package com.sahan.birdspacex.data.local.datasource

import com.sahan.birdspacex.data.local.db.dao.RocketDao
import com.sahan.birdspacex.data.local.db.entity.RocketCacheEntity
import com.sahan.birdspacex.data.remote.response.model.RocketResponseModel
import kotlin.time.Clock

class RocketCacheDataSource(
    private val rocketDao: RocketDao,
) {

    suspend fun getRocket(id: String): RocketResponseModel? {
        val entity = rocketDao.getById(id) ?: return null
        return RocketResponseModel(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            active = null,
            firstFlight = null,
            country = null,
            company = null,
            wikipedia = null,
            flickrImages = emptyList(),
            costPerLaunch = null,
            successRatePct = null,
        )
    }

    suspend fun saveRocket(rocket: RocketResponseModel) {
        val now = Clock.System.now().toEpochMilliseconds()
        rocketDao.insert(
            RocketCacheEntity(
                id = rocket.id,
                name = rocket.name.orEmpty().ifBlank { "Unknown Rocket" },
                description = rocket.description.orEmpty().ifBlank { "No description available." },
                timestamp = now,
            ),
        )
    }

    suspend fun isRocketFresh(id: String, ttlMillis: Long): Boolean {
        val timestamp = rocketDao.timestampById(id) ?: return false
        val now = Clock.System.now().toEpochMilliseconds()
        return now - timestamp <= ttlMillis
    }
}