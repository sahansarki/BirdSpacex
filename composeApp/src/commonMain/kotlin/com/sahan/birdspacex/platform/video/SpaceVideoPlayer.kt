package com.sahan.birdspacex.platform.video

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun SpaceVideoPlayer(
    url: String,
    modifier: Modifier = Modifier,
    autoPlay: Boolean = true,
    showControls: Boolean = true,
)
