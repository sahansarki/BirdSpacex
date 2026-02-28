package com.sahan.birdspacex.di

import com.sahan.birdspacex.data.KtorHttpClientFactory
import com.sahan.birdspacex.data.api.imp.NetworkSpaceXApi
import com.sahan.birdspacex.data.api.`interface`.SpaceXApi
import org.koin.dsl.module

val dataModule = module {
    single { KtorHttpClientFactory(json = get()).create() }
    single<SpaceXApi> {
        NetworkSpaceXApi(client = get())
    }
}