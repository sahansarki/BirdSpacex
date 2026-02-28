package com.sahan.birdspacex.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface SpaceDestination {
    @Serializable
    data object LaunchList : SpaceDestination

    @Serializable
    data class LaunchDetail(val launchId: String) : SpaceDestination
}
