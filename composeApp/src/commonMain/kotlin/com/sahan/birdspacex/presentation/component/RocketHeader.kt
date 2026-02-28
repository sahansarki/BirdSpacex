package com.sahan.birdspacex.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sahan.birdspacex.domain.model.RocketUiModel

@Composable
fun RocketHeader(
    rocket: RocketUiModel?,
    modifier: Modifier = Modifier,
) {
    if (rocket == null) return

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
    ) {
        Text(
            text = rocket.name,
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = rocket.description,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Preview
@Composable
private fun RocketHeaderPreview() {
    RocketHeader(
        rocket = RocketUiModel(
            name = "Falcon 9",
            description = "Reusable rocket family by SpaceX.",
        ),
    )
}
