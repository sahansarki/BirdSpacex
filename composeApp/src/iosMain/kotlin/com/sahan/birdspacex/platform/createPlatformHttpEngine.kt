package com.sahan.birdspacex.platform

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin

actual fun createPlatformHttpEngine(): HttpClientEngine = Darwin.create()