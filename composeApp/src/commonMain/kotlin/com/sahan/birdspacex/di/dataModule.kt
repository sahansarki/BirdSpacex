package com.sahan.birdspacex.di

import com.sahan.birdspacex.data.KtorHttpClientFactory
import com.sahan.birdspacex.data.api.imp.NetworkSpaceXApi
import com.sahan.birdspacex.data.api.`interface`.SpaceXApi
import com.sahan.birdspacex.data.local.datasource.LaunchCacheDataSource
import com.sahan.birdspacex.data.local.datasource.RocketCacheDataSource
import com.sahan.birdspacex.data.local.db.DatabaseFactory
import com.sahan.birdspacex.data.local.db.SpaceDatabase
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
}