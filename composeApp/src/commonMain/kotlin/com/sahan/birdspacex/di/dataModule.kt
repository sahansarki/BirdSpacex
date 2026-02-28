package com.sahan.birdspacex.di

import com.sahan.birdspacex.data.KtorHttpClientFactory
import org.koin.dsl.module

val dataModule = module {
    single { KtorHttpClientFactory(json = get()).create() }
}