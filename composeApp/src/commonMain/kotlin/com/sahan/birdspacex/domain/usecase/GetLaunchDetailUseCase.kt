package com.sahan.birdspacex.domain.usecase

import com.sahan.birdspacex.domain.repository.SpaceRepository

class GetLaunchDetailUseCase(
    private val repository: SpaceRepository,
) {
    suspend operator fun invoke(launchId: String) = repository.getLaunchDetail(launchId = launchId)
}
