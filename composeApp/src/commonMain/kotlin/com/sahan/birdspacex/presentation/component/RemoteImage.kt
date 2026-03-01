package com.sahan.birdspacex.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil3.compose.AsyncImage

@Composable
fun RemoteImage(
    imageUrl: String?,
    modifier: Modifier,
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        modifier = modifier,
    )
}