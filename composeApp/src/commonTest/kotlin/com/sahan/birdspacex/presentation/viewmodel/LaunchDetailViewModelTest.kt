package com.sahan.spacex.presentation.viewmodel

import app.cash.turbine.test
import com.sahan.birdspacex.domain.TestDispatcherProvider
import com.sahan.birdspacex.domain.model.LaunchDetailUiModel
import com.sahan.birdspacex.domain.model.RocketUiModel
import com.sahan.birdspacex.domain.usecase.GetLaunchDetailUseCase
import com.sahan.birdspacex.domain.util.DomainResult
import com.sahan.birdspacex.presentation.mvi.SpaceAction
import com.sahan.birdspacex.presentation.mvi.SpaceEvent
import com.sahan.birdspacex.presentation.viewmodel.LaunchDetailViewModel
import com.sahan.birdspacex.repository.FakeSpacexRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class LaunchDetailViewModelTest {

    @Test
    fun `onAction LoadLaunchDetail updates state with launch detail`() = runTest {
        val expectedDetail = LaunchDetailUiModel(
            missionName = "FalconSat",
            launchDateTimeText = "Mar 24, 2006 22:30",
            rocket = RocketUiModel(
                name = "Falcon 1",
                description = "The Falcon 1 was an expendable launch system.",
            ),
            successText = "Failure",
            details = "Engine failure at 33 seconds",
        )

        val viewModel = LaunchDetailViewModel(
            getLaunchDetailUseCase = GetLaunchDetailUseCase(
                repository = FakeSpacexRepository(
                    detailResult = DomainResult.Success(expectedDetail),
                ),
            ),
            dispatcherProvider = TestDispatcherProvider(StandardTestDispatcher(testScheduler)),
        )

        viewModel.onAction(SpaceAction.LoadLaunchDetail("launch-1"))
        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals(expectedDetail, state.selectedLaunchDetail)
        assertNull(state.error)
    }

    @Test
    fun `onAction OpenExternalLink emits OpenUrl event`() = runTest {
        val viewModel = LaunchDetailViewModel(
            getLaunchDetailUseCase = GetLaunchDetailUseCase(repository = FakeSpacexRepository()),
            dispatcherProvider = TestDispatcherProvider(StandardTestDispatcher(testScheduler)),
        )

        viewModel.events.test {
            viewModel.onAction(SpaceAction.OpenExternalLink("https://www.spacex.com"))
            advanceUntilIdle()

            val event = awaitItem()
            assertEquals(SpaceEvent.OpenUrl("https://www.spacex.com"), event)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
