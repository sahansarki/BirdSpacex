package com.sahan.birdspacex.data.local.datasource

import com.sahan.birdspacex.data.local.db.dao.LaunchDao
import com.sahan.birdspacex.data.local.db.entity.LaunchCacheEntity
import com.sahan.birdspacex.data.remote.response.model.LaunchResponseModel
import com.sahan.birdspacex.data.remote.response.model.LinksResponseModel
import com.sahan.birdspacex.data.remote.response.model.PatchResponseModel
import kotlin.time.Clock

class LaunchCacheDataSource(
    private val launchDao: LaunchDao,
) {

    suspend fun getCachedLaunches(): List<LaunchResponseModel>? {
        val entities = launchDao.getAll()
        if (entities.isEmpty()) return null
        return entities.map { entity ->
            LaunchResponseModel(
                id = entity.id,
                name = entity.missionName,
                dateUtc = entity.dateUtc,
                success = entity.success,
                rocket = entity.rocketId,
                links = LinksResponseModel(
                    patch = PatchResponseModel(
                        small = entity.patchImageUrl,
                        large = null,
                    ),
                ),
            )
        }
    }

    suspend fun saveLaunches(launches: List<LaunchResponseModel>) {
        val now = Clock.System.now().toEpochMilliseconds()
        launchDao.clear()
        launchDao.insertAll(
            launches.map { launch ->
                LaunchCacheEntity(
                    id = launch.id,
                    missionName = launch.name.orEmpty().ifBlank { "Unknown Mission" },
                    dateUtc = launch.dateUtc.orEmpty(),
                    rocketId = launch.rocket.orEmpty(),
                    success = launch.success,
                    patchImageUrl = launch.links?.patch?.small ?: launch.links?.patch?.large,
                    timestamp = now,
                )
            },
        )
    }

    suspend fun isFresh(ttlMillis: Long): Boolean {
        val latestTimestamp = launchDao.latestTimestamp() ?: return false
        val now = Clock.System.now().toEpochMilliseconds()
        return now - latestTimestamp <= ttlMillis
    }
}