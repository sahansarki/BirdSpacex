package com.sahan.birdspacex

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.sahan.birdspacex.presentation.navigation.SpaceNavGraph

@Composable
fun App() {
    MaterialTheme {
        SpaceNavGraph()
    }
}
