package com.sahan.birdspacex.presentation.mvi

import com.sahan.birdspacex.domain.model.LaunchDetailUiModel
import com.sahan.birdspacex.domain.model.LaunchListItemUiModel

data class SpaceUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: UiError? = null,
    val launches: List<LaunchListItemUiModel> = emptyList(),
    val selectedLaunchDetail: LaunchDetailUiModel? = null,
)

data class UiError(
    val title: String,
    val message: String,
)
