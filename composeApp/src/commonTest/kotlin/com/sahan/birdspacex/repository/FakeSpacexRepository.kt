package com.sahan.birdspacex.repository

import com.sahan.birdspacex.domain.model.LaunchDetailUiModel
import com.sahan.birdspacex.domain.model.LaunchListItemUiModel
import com.sahan.birdspacex.domain.repository.SpaceRepository
import com.sahan.birdspacex.domain.util.DomainResult
import com.sahan.birdspacex.domain.util.NetworkError

class FakeSpacexRepository(
    private val launchesResult: DomainResult<List<LaunchListItemUiModel>> = DomainResult.Success(emptyList()),
    private val detailResult: DomainResult<LaunchDetailUiModel> = DomainResult.Error(NetworkError.Unknown()),
) : SpaceRepository {
    override suspend fun getLaunches(forceRefresh: Boolean): DomainResult<List<LaunchListItemUiModel>> {
        return launchesResult
    }

    override suspend fun getLaunchDetail(launchId: String): DomainResult<LaunchDetailUiModel> {
        return detailResult
    }
}