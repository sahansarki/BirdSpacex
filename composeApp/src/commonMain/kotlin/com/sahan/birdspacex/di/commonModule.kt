package com.sahan.birdspacex.di

import com.sahan.birdspacex.platform.PlatformDateFormatter
import com.sahan.birdspacex.platform.PlatformUrlOpener
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val commonModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            explicitNulls = false
        }
    }
    single { PlatformDateFormatter() }
    single { PlatformUrlOpener() }
}
