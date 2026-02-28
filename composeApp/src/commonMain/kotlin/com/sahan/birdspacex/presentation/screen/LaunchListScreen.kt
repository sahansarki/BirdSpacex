package com.sahan.birdspacex.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sahan.birdspacex.domain.model.LaunchListItemUiModel
import com.sahan.birdspacex.domain.model.LaunchStatus
import com.sahan.birdspacex.presentation.component.ErrorView
import com.sahan.birdspacex.presentation.component.LaunchListItem
import com.sahan.birdspacex.presentation.component.LoadingView
import com.sahan.birdspacex.presentation.mvi.SpaceAction
import com.sahan.birdspacex.presentation.mvi.SpaceEvent
import com.sahan.birdspacex.presentation.mvi.SpaceUiState
import com.sahan.birdspacex.presentation.viewmodel.LaunchListViewModel
import org.koin.mp.KoinPlatform

@Composable
fun LaunchListRoute(
    onNavigateToDetail: (String) -> Unit,
) {
    val viewModel = remember { KoinPlatform.getKoin().get<LaunchListViewModel>() }
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.onAction(SpaceAction.LoadLaunches)
    }

    LaunchedEffect(viewModel, onNavigateToDetail) {
        viewModel.events.collect { event ->
            when (event) {
                is SpaceEvent.NavigateToDetail -> onNavigateToDetail(event.launchId)
                is SpaceEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
                is SpaceEvent.OpenUrl -> Unit
            }
        }
    }

    LaunchListContent(
        state = state,
        onAction = viewModel::onAction,
        snackbarHostState = snackbarHostState,
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LaunchListContent(
    state: SpaceUiState,
    onAction: (SpaceAction) -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Launch Liste") },
                actions = {
                    IconButton(onClick = { onAction(SpaceAction.Refresh()) }) {
                        Text("Güncelle")
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
        ) {
            when {
                state.isLoading && state.launches.isEmpty() -> LoadingView(message = "Yükleniyor..")

                state.error != null && state.launches.isEmpty() -> ErrorView(
                    title = state.error.title,
                    message = state.error.message,
                    onRetry = { onAction(SpaceAction.Retry) },
                )

                state.launches.isEmpty() -> EmptyLaunchesView()

                else -> LaunchList(
                    launches = state.launches,
                    onLaunchClick = { launchId -> onAction(SpaceAction.OnLaunchClick(launchId)) },
                )
            }

            if (state.isRefreshing) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
private fun LaunchList(
    launches: List<LaunchListItemUiModel>,
    onLaunchClick: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(items = launches, key = { it.id }) { launch ->
            LaunchListItem(item = launch, onClick = onLaunchClick)
        }
    }
}

@Composable
private fun EmptyLaunchesView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "Herhangi bir data yok.",
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Preview
@Composable
private fun LaunchListContentPreview() {
    LaunchListContent(
        state = SpaceUiState(
            launches = listOf(
                LaunchListItemUiModel(
                    id = "1",
                    missionName = "Trailblazer",
                    launchDateText = "Ağustos 03, 2008",
                    rocketName = "Falcon 1",
                    status = LaunchStatus.FAILURE,
                    successText = "Başarısız",
                ),
            ),
        ),
        onAction = {},
        snackbarHostState = remember { SnackbarHostState() },
    )
}
