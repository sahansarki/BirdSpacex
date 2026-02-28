package com.sahan.birdspacex.domain.model

data class LaunchDetailUiModel(
    val missionName: String,
    val launchDateTimeText: String,
    val rocket: RocketUiModel?,
    val successText: String,
    val details: String? = null,
    val webcastUrl: String? = null,
    val links: ExternalLinksUiModel = ExternalLinksUiModel(),
)
