package com.sahan.birdspacex.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sahan.birdspacex.domain.model.ExternalLinksUiModel
import com.sahan.birdspacex.domain.model.LaunchDetailUiModel
import com.sahan.birdspacex.domain.model.RocketUiModel
import com.sahan.birdspacex.platform.PlatformUrlOpener
import com.sahan.birdspacex.platform.video.SpaceVideoPlayer
import com.sahan.birdspacex.presentation.component.ErrorView
import com.sahan.birdspacex.presentation.component.LoadingView
import com.sahan.birdspacex.presentation.component.RocketHeader
import com.sahan.birdspacex.presentation.mvi.SpaceAction
import com.sahan.birdspacex.presentation.mvi.SpaceEvent
import com.sahan.birdspacex.presentation.mvi.SpaceUiState
import com.sahan.birdspacex.presentation.viewmodel.LaunchDetailViewModel
import org.koin.mp.KoinPlatform

@Composable
fun LaunchDetailRoute(
    launchId: String,
    onBack: () -> Unit,
) {
    val viewModel = remember { KoinPlatform.getKoin().get<LaunchDetailViewModel>() }
    val urlOpener = remember { KoinPlatform.getKoin().get<PlatformUrlOpener>() }
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(launchId, viewModel) {
        viewModel.onAction(SpaceAction.LoadLaunchDetail(launchId))
    }

    LaunchedEffect(viewModel, urlOpener) {
        viewModel.events.collect { event ->
            when (event) {
                is SpaceEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
                is SpaceEvent.OpenUrl -> urlOpener.open(event.url)
                is SpaceEvent.NavigateToDetail -> Unit
            }
        }
    }

    LaunchDetailContent(
        state = state,
        onAction = viewModel::onAction,
        onBack = onBack,
        snackbarHostState = snackbarHostState,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaunchDetailContent(
    state: SpaceUiState,
    onAction: (SpaceAction) -> Unit,
    onBack: () -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Launch Detail") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("Geri")
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        when {
            state.isLoading && state.selectedLaunchDetail == null -> LoadingView("Yükleniyor..")

            state.error != null && state.selectedLaunchDetail == null -> ErrorView(
                title = state.error.title,
                message = state.error.message,
                onRetry = { onAction(SpaceAction.Retry) },
            )

            state.selectedLaunchDetail != null -> DetailBody(
                detail = state.selectedLaunchDetail,
                onOpenLink = { link -> onAction(SpaceAction.OpenExternalLink(link)) },
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
            )

            else -> Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "No detail found",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp),
                )
            }
        }
    }
}

@Composable
private fun DetailBody(
    detail: LaunchDetailUiModel,
    onOpenLink: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(16.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(text = detail.missionName, style = MaterialTheme.typography.headlineSmall)
        Text(text = detail.launchDateTimeText, style = MaterialTheme.typography.bodyMedium)
        Text(text = "Durum: ${detail.successText}", style = MaterialTheme.typography.bodyMedium)

        val webcastUrl = detail.webcastUrl.orEmpty()
        if (webcastUrl.isNotBlank()) {
            if (LocalInspectionMode.current) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .background(Color.Black),
                )
            } else {
                SpaceVideoPlayer(
                    url = webcastUrl,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f),
                )
            }
        }

        Text(
            text = detail.details ?: "-",
            style = MaterialTheme.typography.bodyMedium
        )

        RocketHeader(
            rocket = detail.rocket,
            modifier = Modifier.fillMaxWidth(),
        )

        detail.links.article?.let { link ->
            Button(onClick = { onOpenLink(link) }) {
                Text("Makale")
            }
        }
        detail.links.wikipedia?.let { link ->
            Button(onClick = { onOpenLink(link) }) {
                Text("Vikipedia")
            }
        }
    }
}

@Preview
@Composable
private fun LaunchDetailContentPreview() {
    LaunchDetailContent(
        state = SpaceUiState(
            selectedLaunchDetail = LaunchDetailUiModel(
                missionName = "FalconSat",
                launchDateTimeText = "Mart 24, 2006 22:30",
                rocket = RocketUiModel(
                    name = "Falcon 1",
                    description = "Fırlatma Sistemi",
                ),
                successText = "Başarısız",
                details = "Motor arızası",
                webcastUrl = "-",
                links = ExternalLinksUiModel(
                    article = "-",
                    wikipedia = "-",
                ),
            ),
        ),
        onAction = {},
        onBack = {},
        snackbarHostState = remember { SnackbarHostState() },
    )
}
