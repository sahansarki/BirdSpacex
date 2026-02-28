package com.sahan.spacex.presentation.viewmodel

import app.cash.turbine.test
import com.sahan.birdspacex.domain.TestDispatcherProvider
import com.sahan.birdspacex.domain.model.LaunchListItemUiModel
import com.sahan.birdspacex.domain.model.LaunchStatus
import com.sahan.birdspacex.domain.usecase.GetLaunchesUseCase
import com.sahan.birdspacex.domain.util.DomainResult
import com.sahan.birdspacex.presentation.mvi.SpaceAction
import com.sahan.birdspacex.presentation.mvi.SpaceEvent
import com.sahan.birdspacex.presentation.viewmodel.LaunchListViewModel
import com.sahan.birdspacex.repository.FakeSpacexRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class LaunchListViewModelTest {

    @Test
    fun `onAction LoadLaunches updates state with launches`() = runTest {
        val viewModel = LaunchListViewModel(
            getLaunchesUseCase = GetLaunchesUseCase(
                repository = FakeSpacexRepository(
                    launchesResult = DomainResult.Success(
                        data = listOf(
                            LaunchListItemUiModel(
                                id = "1",
                                missionName = "FalconSat",
                                launchDateText = "Mar 24, 2006",
                                rocketName = "Falcon 1",
                                status = LaunchStatus.FAILURE,
                                successText = "Failure",
                            ),
                        ),
                    ),
                ),
            ),
            dispatcherProvider = TestDispatcherProvider(StandardTestDispatcher(testScheduler)),
        )

        viewModel.onAction(SpaceAction.LoadLaunches)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertTrue(state.launches.isNotEmpty())
    }

    @Test
    fun `onAction OnLaunchClick emits NavigateToDetail event`() = runTest {
        val viewModel = LaunchListViewModel(
            getLaunchesUseCase = GetLaunchesUseCase(repository = FakeSpacexRepository()),
            dispatcherProvider = TestDispatcherProvider(StandardTestDispatcher(testScheduler)),
        )

        viewModel.events.test {
            viewModel.onAction(SpaceAction.OnLaunchClick("launch-123"))
            advanceUntilIdle()

            val event = awaitItem()
            assertEquals(SpaceEvent.NavigateToDetail("launch-123"), event)
            cancelAndIgnoreRemainingEvents()
        }
    }

}
