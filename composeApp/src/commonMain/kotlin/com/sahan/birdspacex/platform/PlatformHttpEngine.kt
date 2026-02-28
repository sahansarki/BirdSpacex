package com.sahan.birdspacex.platform

import io.ktor.client.engine.HttpClientEngine

expect fun createPlatformHttpEngine(): HttpClientEngine
