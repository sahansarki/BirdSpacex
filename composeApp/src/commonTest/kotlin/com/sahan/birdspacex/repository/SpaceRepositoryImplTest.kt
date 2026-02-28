package com.sahan.spacex.data.repository

import com.sahan.birdspacex.data.api.`interface`.SpaceXApi
import com.sahan.birdspacex.data.local.datasource.LaunchCacheDataSource
import com.sahan.birdspacex.data.local.datasource.RocketCacheDataSource
import com.sahan.birdspacex.data.local.db.dao.LaunchDao
import com.sahan.birdspacex.data.local.db.dao.RocketDao
import com.sahan.birdspacex.data.local.db.entity.LaunchCacheEntity
import com.sahan.birdspacex.data.local.db.entity.RocketCacheEntity
import com.sahan.birdspacex.data.mapper.LaunchDetailMapper
import com.sahan.birdspacex.data.mapper.LaunchListItemMapper
import com.sahan.birdspacex.data.mapper.RocketDetailMapper
import com.sahan.birdspacex.data.remote.response.model.LaunchResponseModel
import com.sahan.birdspacex.data.remote.response.model.LinksResponseModel
import com.sahan.birdspacex.data.remote.response.model.PatchResponseModel
import com.sahan.birdspacex.data.remote.response.model.RocketResponseModel
import com.sahan.birdspacex.data.repository.SpaceRepositoryImpl
import com.sahan.birdspacex.domain.util.DomainResult
import com.sahan.birdspacex.platform.PlatformDateFormatter
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertTrue

class SpaceRepositoryImplTest {
    private fun createRepository(): SpaceRepositoryImpl {
        val launchDao = FakeLaunchDao()
        val rocketDao = FakeRocketDao()
        return SpaceRepositoryImpl(
            api = FakeSpaceXApi(),
            launchCacheDataSource = LaunchCacheDataSource(launchDao = launchDao),
            rocketCacheDataSource = RocketCacheDataSource(rocketDao = rocketDao),
            launchListItemMapper = LaunchListItemMapper(dateFormatter = PlatformDateFormatter()),
            rocketDetailMapper = RocketDetailMapper(),
            launchDetailMapper = LaunchDetailMapper(dateFormatter = PlatformDateFormatter()),
        )
    }

    @Test
    fun `getLaunches returns mapped list`() = runTest {
        val repository = createRepository()

        val result = repository.getLaunches(forceRefresh = true)

        assertTrue(result is DomainResult.Success)
        assertTrue(result.data.isNotEmpty())
    }

    @Test
    fun `getLaunchDetail returns combined launch detail`() = runTest {
        val repository = createRepository()
        val launchesResult = repository.getLaunches(forceRefresh = true)
        assertTrue(launchesResult is DomainResult.Success)

        val launchId = launchesResult.data.first().id
        val detailResult = repository.getLaunchDetail(launchId)

        assertTrue(detailResult is DomainResult.Success)
        assertTrue(detailResult.data.missionName.isNotBlank())
        assertTrue(detailResult.data.rocket != null)
    }

    private class FakeSpaceXApi : SpaceXApi {
        private val launches = listOf(
            LaunchResponseModel(
                id = "launch-1",
                name = "FalconSat",
                dateUtc = "2006-03-24T22:30:00.000Z",
                success = false,
                rocket = "rocket-1",
                details = "Engine failure at 33 seconds and loss of vehicle",
                links = LinksResponseModel(
                    patch = PatchResponseModel(
                        small = "https://images2.imgbox.com/94/f2/NN6Ph45r_o.png",
                        large = "https://images2.imgbox.com/5b/02/QcxHUb5V_o.png",
                    ),
                    webcast = "https://www.youtube.com/watch?v=0a_00nJ_Y88",
                    article = "https://www.space.com/2196-spacex-inaugural-falcon-1-rocket-lost-launch.html",
                    wikipedia = "https://en.wikipedia.org/wiki/DemoSat",
                ),
            ),
        )

        private val rocket = RocketResponseModel(
            id = "rocket-1",
            name = "Falcon 1",
            description = "The Falcon 1 was an expendable launch system.",
            active = false,
            firstFlight = "2006-03-24",
            country = "Republic of the Marshall Islands",
            company = "SpaceX",
            wikipedia = "https://en.wikipedia.org/wiki/Falcon_1",
            flickrImages = listOf("https://imgur.com/DaCfMsj.jpg"),
            costPerLaunch = 6700000,
            successRatePct = 40,
        )

        override suspend fun getLaunches(): List<LaunchResponseModel> = launches

        override suspend fun getLaunch(id: String): LaunchResponseModel {
            return launches.first { it.id == id }
        }

        override suspend fun getRocket(id: String): RocketResponseModel {
            return rocket.copy(id = id)
        }
    }

    private class FakeLaunchDao : LaunchDao {
        private val storage = linkedMapOf<String, LaunchCacheEntity>()

        override suspend fun getAll(): List<LaunchCacheEntity> {
            return storage.values.toList()
        }

        override suspend fun insertAll(items: List<LaunchCacheEntity>) {
            items.forEach { storage[it.id] = it }
        }

        override suspend fun clear() {
            storage.clear()
        }

        override suspend fun latestTimestamp(): Long? {
            return storage.values.maxOfOrNull { it.timestamp }
        }
    }

    private class FakeRocketDao : RocketDao {
        private val storage = linkedMapOf<String, RocketCacheEntity>()

        override suspend fun getAll(): List<RocketCacheEntity> {
            return storage.values.toList()
        }

        override suspend fun getById(id: String): RocketCacheEntity? {
            return storage[id]
        }

        override suspend fun insert(item: RocketCacheEntity) {
            storage[item.id] = item
        }

        override suspend fun insertAll(items: List<RocketCacheEntity>) {
            items.forEach { storage[it.id] = it }
        }

        override suspend fun clear() {
            storage.clear()
        }

        override suspend fun timestampById(id: String): Long? {
            return storage[id]?.timestamp
        }
    }
}
