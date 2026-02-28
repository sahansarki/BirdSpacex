package com.sahan.birdspacex.di

import com.sahan.birdspacex.data.remote.KtorHttpClientFactory
import com.sahan.birdspacex.data.api.imp.NetworkSpaceXApi
import com.sahan.birdspacex.data.api.`interface`.SpaceXApi
import com.sahan.birdspacex.data.local.datasource.LaunchCacheDataSource
import com.sahan.birdspacex.data.local.datasource.RocketCacheDataSource
import com.sahan.birdspacex.data.local.db.DatabaseFactory
import com.sahan.birdspacex.data.local.db.SpaceDatabase
import com.sahan.birdspacex.data.mapper.LaunchDetailMapper
import com.sahan.birdspacex.data.mapper.LaunchListItemMapper
import com.sahan.birdspacex.data.mapper.RocketDetailMapper
import com.sahan.birdspacex.data.repository.SpaceRepositoryImpl
import com.sahan.birdspacex.domain.repository.SpaceRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataModule = module {
    single { KtorHttpClientFactory(json = get()).create() }
    single<SpaceXApi> {
        NetworkSpaceXApi(client = get())
    }
    single { DatabaseFactory(getOrNull<Any>(qualifier = named(PLATFORM_CONTEXT_QUALIFIER))) }
    single { get<DatabaseFactory>().create() }
    single { get<SpaceDatabase>().launchDao() }
    single { get<SpaceDatabase>().rocketDao() }
    single { LaunchCacheDataSource(launchDao = get()) }
    single { RocketCacheDataSource(rocketDao = get()) }

    single { LaunchListItemMapper(dateFormatter = get()) }
    single { RocketDetailMapper() }
    single { LaunchDetailMapper(dateFormatter = get()) }

    single<SpaceRepository> {
        SpaceRepositoryImpl(
            api = get(),
            launchCacheDataSource = get(),
            rocketCacheDataSource = get(),
            launchListItemMapper = get(),
            rocketDetailMapper = get(),
            launchDetailMapper = get()
        )
    }
}