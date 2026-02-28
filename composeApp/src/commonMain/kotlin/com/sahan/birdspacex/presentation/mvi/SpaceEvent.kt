package com.sahan.birdspacex.presentation.mvi

sealed interface SpaceEvent {
    data class NavigateToDetail(val launchId: String) : SpaceEvent
    data class ShowSnackbar(val message: String) : SpaceEvent
    data class OpenUrl(val url: String) : SpaceEvent
}
