package com.sahan.birdspacex.network
import com.sahan.birdspacex.BuildConfig

actual object AppConfig {
    actual val BASE_URL: String
        get() = BuildConfig.BASE_URL
}
