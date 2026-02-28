package com.sahan.birdspacex.domain.repository

import com.sahan.birdspacex.domain.model.LaunchDetailUiModel
import com.sahan.birdspacex.domain.model.LaunchListItemUiModel
import com.sahan.birdspacex.domain.util.DomainResult

interface SpaceRepository {
    suspend fun getLaunches(forceRefresh: Boolean): DomainResult<List<LaunchListItemUiModel>>
    suspend fun getLaunchDetail(launchId: String): DomainResult<LaunchDetailUiModel>
}
