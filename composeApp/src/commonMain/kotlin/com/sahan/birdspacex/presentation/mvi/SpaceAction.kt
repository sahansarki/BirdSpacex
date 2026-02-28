package com.sahan.birdspacex.presentation.mvi

sealed interface SpaceAction {
    data object LoadLaunches : SpaceAction
    data class Refresh(val force: Boolean = true) : SpaceAction
    data class OnLaunchClick(val launchId: String) : SpaceAction

    data class LoadLaunchDetail(val launchId: String) : SpaceAction
    data object Retry : SpaceAction
    data class OpenExternalLink(val url: String) : SpaceAction
}
