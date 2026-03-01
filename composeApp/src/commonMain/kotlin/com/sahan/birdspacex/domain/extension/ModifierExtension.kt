package com.sahan.birdspacex.domain.extension

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import kotlin.time.Clock

fun Modifier.preventDoubleClickNoRipple(
    debounceTime: Long = 1000L,
    onClick: () -> Unit
): Modifier = composed(
    inspectorInfo = {
        name = "preventDoubleClickNoRipple"
        value = onClick
    }
) {
    var lastClickTime by remember { mutableStateOf(0L) }

    this.clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    ) {
        val now = Clock.System.now().epochSeconds

        if (now - lastClickTime >= debounceTime) {
            lastClickTime = now
            onClick()
        }
    }
}
