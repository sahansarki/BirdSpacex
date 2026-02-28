package com.sahan.birdspacex.data.remote

import com.sahan.birdspacex.platform.createPlatformHttpEngine
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class KtorHttpClientFactory(
    private val json: Json,
) {
    fun create(): HttpClient {
        return HttpClient(createPlatformHttpEngine()) {
            HttpClientConfig.install(ContentNegotiation) {
                json(json)
            }
        }
    }
}