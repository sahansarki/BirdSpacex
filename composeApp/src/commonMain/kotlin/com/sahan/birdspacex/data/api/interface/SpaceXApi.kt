package com.sahan.birdspacex.data.api.`interface`

import com.sahan.birdspacex.data.response.model.LaunchResponseModel
import com.sahan.birdspacex.data.response.model.RocketResponseModel

interface SpaceXApi {
    suspend fun getLaunches(): List<LaunchResponseModel>
    suspend fun getLaunch(id: String): LaunchResponseModel
    suspend fun getRocket(id: String): RocketResponseModel
}