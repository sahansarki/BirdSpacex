package com.sahan.birdspacex.presentation.viewmodel

import com.sahan.birdspacex.domain.DefaultDispatcherProvider
import com.sahan.birdspacex.domain.usecase.GetLaunchesUseCase
import com.sahan.birdspacex.domain.util.DispatcherProvider
import com.sahan.birdspacex.domain.util.DomainResult
import com.sahan.birdspacex.domain.util.NetworkError
import com.sahan.birdspacex.presentation.mvi.SpaceAction
import com.sahan.birdspacex.presentation.mvi.SpaceEvent
import com.sahan.birdspacex.presentation.mvi.UiError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LaunchListViewModel(
    private val getLaunchesUseCase: GetLaunchesUseCase,
    dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
) : SpaceViewModel(dispatcherProvider) {

    private var loadInProgress = false

    override fun onAction(action: SpaceAction) {
        when (action) {
            SpaceAction.LoadLaunches -> loadLaunches(
                forceRefresh = false,
                userInitiated = false,
                refreshAfterCache = true
            )

            is SpaceAction.Refresh -> loadLaunches(
                forceRefresh = action.force,
                userInitiated = true,
                refreshAfterCache = false
            )

            is SpaceAction.OnLaunchClick -> {
                viewModelScope.launch {
                    emitEvent(SpaceEvent.NavigateToDetail(action.launchId))
                }
            }

            SpaceAction.Retry -> loadLaunches(
                forceRefresh = true,
                userInitiated = true,
                refreshAfterCache = false
            )

            is SpaceAction.LoadLaunchDetail,
            is SpaceAction.OpenExternalLink,
                -> Unit
        }
    }

    private fun loadLaunches(
        forceRefresh: Boolean,
        userInitiated: Boolean,
        refreshAfterCache: Boolean,
    ) {
        if (loadInProgress) return

        viewModelScope.launch {
            loadInProgress = true
            updateState {
                if (userInitiated) {
                    it.copy(isRefreshing = true, error = null)
                } else {
                    it.copy(isLoading = true, error = null)
                }
            }

            when (val result = getLaunchesUseCase(forceRefresh = forceRefresh)) {
                is DomainResult.Success -> {
                    updateState {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            launches = result.data,
                            error = null,
                        )
                    }

                    if (result.fromCache && forceRefresh) {
                        emitEvent(SpaceEvent.ShowSnackbar("Cache data gösteriliyor."))
                    }

                    if (result.fromCache && refreshAfterCache) {
                        loadInProgress = false
                        loadLaunches(
                            forceRefresh = true,
                            userInitiated = false,
                            refreshAfterCache = false
                        )
                        return@launch
                    }
                }

                is DomainResult.Error -> {
                    updateState {
                        it.copy(
                            isLoading = false, isRefreshing = false,
                            error = UiError(
                                title = "HATA",
                                message = result.error.toReadableMessage(),
                            ),
                        )
                    }
                }
            }

            loadInProgress = false
        }
    }
}

private fun NetworkError.toReadableMessage(): String {
    return when (this) {
        NetworkError.NoInternet -> "Internet bağlantınızı kontrol ediniz."
        NetworkError.Timeout -> "Bir hata oluştu."
        NetworkError.Serialization -> "Bir hata oluştu."
        is NetworkError.Server -> "Bir hata oluştu."
        is NetworkError.Unknown -> "Bir hata oluştu."
    }
}
