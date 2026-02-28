package com.sahan.birdspacex.domain.usecase

import com.sahan.birdspacex.domain.repository.SpaceRepository

class GetLaunchesUseCase(
    private val repository: SpaceRepository,
) {
    suspend operator fun invoke(forceRefresh: Boolean) = repository.getLaunches(forceRefresh = forceRefresh)
}
