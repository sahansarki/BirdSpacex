package com.sahan.birdspacex.platform.video

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.cinterop.ExperimentalForeignApi

@Composable
@OptIn(ExperimentalForeignApi::class)
actual fun SpaceVideoPlayer(
    url: String,
    modifier: Modifier,
    autoPlay: Boolean,
    showControls: Boolean,
) {
    YoutubePlayerView(
        videoId = url,
        modifier = modifier,
    )
}
