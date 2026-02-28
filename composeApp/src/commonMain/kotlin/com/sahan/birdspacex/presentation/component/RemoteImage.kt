package com.sahan.birdspacex.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil3.compose.AsyncImage

@Composable
fun RemoteImage(
    imageUrl: String?,
    modifier: Modifier,
) {
//    if (imageUrl.isNullOrBlank()) {
//        Box(
//            modifier = modifier.background(MaterialTheme.colorScheme.secondaryContainer),
//            contentAlignment = Alignment.Center,
//        ) {
//            Text(text = "N/A", color = MaterialTheme.colorScheme.onSecondaryContainer)
//        }
//        return
//    }

    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        modifier = modifier,
    )
}