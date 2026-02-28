package com.sahan.birdspacex.data.repository

import com.sahan.birdspacex.data.api.`interface`.SpaceXApi
import com.sahan.birdspacex.data.local.datasource.LaunchCacheDataSource
import com.sahan.birdspacex.data.local.datasource.RocketCacheDataSource
import com.sahan.birdspacex.data.mapper.LaunchDetailMapper
import com.sahan.birdspacex.data.mapper.LaunchDetailSource
import com.sahan.birdspacex.data.mapper.LaunchListItemMapper
import com.sahan.birdspacex.data.mapper.RocketDetailMapper
import com.sahan.birdspacex.data.remote.response.model.LaunchResponseModel
import com.sahan.birdspacex.data.remote.safeApiCall
import com.sahan.birdspacex.domain.model.LaunchDetailUiModel
import com.sahan.birdspacex.domain.model.LaunchListItemUiModel
import com.sahan.birdspacex.domain.model.RocketUiModel
import com.sahan.birdspacex.domain.repository.SpaceRepository
import com.sahan.birdspacex.domain.util.DomainResult
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit

class SpaceRepositoryImpl(
    private val api: SpaceXApi,
    private val launchCacheDataSource: LaunchCacheDataSource,
    private val rocketCacheDataSource: RocketCacheDataSource,
    private val launchListItemMapper: LaunchListItemMapper,
    private val rocketDetailMapper: RocketDetailMapper,
    private val launchDetailMapper: LaunchDetailMapper,
) : SpaceRepository {

    override suspend fun getLaunches(forceRefresh: Boolean): DomainResult<List<LaunchListItemUiModel>> {
        val cachedLaunches = launchCacheDataSource.getCachedLaunches()
        val canUseFreshCache = !forceRefresh && launchCacheDataSource.isFresh(CACHE_TTL_MILLIS)

        if (canUseFreshCache && !cachedLaunches.isNullOrEmpty()) {
            return DomainResult.Success(data = mapLaunches(cachedLaunches), fromCache = true)
        }

        when (val networkResult = safeApiCall { api.getLaunches() }) {
            is DomainResult.Success -> {
                val launches = networkResult.data
                launchCacheDataSource.saveLaunches(launches)
                return DomainResult.Success(data = mapLaunches(launches), fromCache = false)
            }

            is DomainResult.Error -> {
                if (!cachedLaunches.isNullOrEmpty()) {
                    return DomainResult.Success(
                        data = mapLaunches(cachedLaunches),
                        fromCache = true
                    )
                }
                return DomainResult.Error(
                    error = networkResult.error,
                    fallbackDataAvailable = false,
                )
            }
        }
    }

    override suspend fun getLaunchDetail(launchId: String): DomainResult<LaunchDetailUiModel> {
        var fromCache = false
        val launch = when (val launchResult = safeApiCall { api.getLaunch(launchId) }) {
            is DomainResult.Success -> launchResult.data
            is DomainResult.Error -> {
                val fallback = launchCacheDataSource.getCachedLaunches()
                    ?.firstOrNull { it.id == launchId }
                if (fallback != null) {
                    fromCache = true
                    fallback
                } else {
                    return DomainResult.Error(
                        error = launchResult.error,
                        fallbackDataAvailable = false,
                    )
                }
            }
        }

        val rocketUiModel = resolveRocketUiModel(launch.rocket)
        return DomainResult.Success(
            data = launchDetailMapper.map(
                LaunchDetailSource(
                    launch = launch,
                    rocket = rocketUiModel
                )
            ),
            fromCache = fromCache,
        )
    }

    private fun mapLaunches(launches: List<LaunchResponseModel>): List<LaunchListItemUiModel> {
        return launches.map { launch ->
            launchListItemMapper.map(launch)
        }
    }


    private suspend fun resolveRocketUiModel(rocketId: String?): RocketUiModel? {
        rocketId ?: return null

        val cachedRocket = rocketCacheDataSource.getRocket(rocketId)
        val isFreshCache = rocketCacheDataSource.isRocketFresh(rocketId, CACHE_TTL_MILLIS)
        if (cachedRocket != null && isFreshCache) {
            val mapped = rocketDetailMapper.map(cachedRocket)
            if (mapped != null) {
                return mapped
            }
        }

        return when (val rocketResult = safeApiCall { api.getRocket(rocketId) }) {
            is DomainResult.Success -> {
                rocketCacheDataSource.saveRocket(rocketResult.data)
                rocketDetailMapper.map(rocketResult.data)
            }

            is DomainResult.Error -> {
                if (cachedRocket != null) {
                    rocketDetailMapper.map(cachedRocket)
                }
                RocketUiModel(
                    name = "-",
                    description = "-",
                )
            }
        }
    }

    private companion object {
        const val CACHE_TTL_MILLIS = 6 * 60 * 60 * 1000L
    }
}
