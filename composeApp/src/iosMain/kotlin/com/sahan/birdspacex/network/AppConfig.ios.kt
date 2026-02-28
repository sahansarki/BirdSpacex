package com.sahan.birdspacex.network

import platform.Foundation.NSBundle

actual object AppConfig {
    actual val BASE_URL: String by lazy {
        (NSBundle.mainBundle.objectForInfoDictionaryKey("BASE_URL") as? String)
            ?.takeIf { it.isNotBlank() }
            ?: "https://api.spacexdata.com/v4/"
    }
}
