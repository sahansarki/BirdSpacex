package com.sahan.birdspacex

import androidx.compose.ui.window.ComposeUIViewController
import com.sahan.birdspacex.di.initKoin

fun doInitKoin() {
    initKoin()
}
fun MainViewController() = ComposeUIViewController { App() }