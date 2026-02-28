package com.sahan.birdspacex.domain.model

data class LaunchListItemUiModel(
    val id: String,
    val missionName: String,
    val launchDateText: String,
    val rocketName: String,
    val status: LaunchStatus,
    val successText: String,
    val patchImageUrl: String? = null,
)
