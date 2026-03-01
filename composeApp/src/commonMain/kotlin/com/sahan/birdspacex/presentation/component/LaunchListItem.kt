package com.sahan.birdspacex.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sahan.birdspacex.domain.model.LaunchListItemUiModel
import com.sahan.birdspacex.domain.model.LaunchStatus

@Composable
fun LaunchListItem(
    item: LaunchListItemUiModel,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(item.id) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(12.dp),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                RemoteImage(
                    imageUrl = item.patchImageUrl,
                    modifier = Modifier
                        .size(52.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(12.dp),
                        ),
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.missionName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = item.launchDateText,
                    style = MaterialTheme.typography.bodySmall,
                )
                Text(
                    text = item.rocketName,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(modifier = Modifier.height(6.dp))
                LaunchStatusChip(statusText = item.successText, status = item.status)
            }
        }
    }
}

@Composable
private fun LaunchStatusChip(statusText: String, status: LaunchStatus) {
    val color = when (status) {
        LaunchStatus.SUCCESS -> MaterialTheme.colorScheme.primary
        LaunchStatus.FAILURE -> MaterialTheme.colorScheme.error
        LaunchStatus.UNKNOWN -> MaterialTheme.colorScheme.tertiary
    }

    Box(
        modifier = Modifier
            .background(color = color, shape = RoundedCornerShape(999.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
    ) {
        Text(
            text = statusText,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

@Preview
@Composable
private fun LaunchListItemPreview() {
    LaunchListItem(
        item = LaunchListItemUiModel(
            id = "1",
            missionName = "FalconSat",
            launchDateText = "March 24, 2006",
            rocketName = "Falcon 1",
            status = LaunchStatus.FAILURE,
            successText = "Failure",
            patchImageUrl = "",
        ),
        onClick = {},
    )
}
