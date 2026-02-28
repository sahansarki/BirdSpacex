package com.sahan.birdspacex.data.api.imp

import com.sahan.birdspacex.data.api.`interface`.SpaceXApi
import com.sahan.birdspacex.data.response.model.LaunchResponseModel
import com.sahan.birdspacex.data.response.model.RocketResponseModel
import com.sahan.birdspacex.network.AppConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class NetworkSpaceXApi(
    private val client: HttpClient,
) : SpaceXApi {

    override suspend fun getLaunches(): List<LaunchResponseModel> {
        return client.get("${AppConfig.BASE_URL}launches").body()
    }

    override suspend fun getLaunch(id: String): LaunchResponseModel {
        return client.get("${AppConfig.BASE_URL}launches/$id").body()
    }

    override suspend fun getRocket(id: String): RocketResponseModel {
        return client.get("${AppConfig.BASE_URL}rockets/$id").body()
    }
}