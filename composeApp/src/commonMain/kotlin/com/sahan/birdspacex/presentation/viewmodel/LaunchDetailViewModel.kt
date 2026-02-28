package com.sahan.birdspacex.presentation.viewmodel

import com.sahan.birdspacex.domain.DefaultDispatcherProvider
import com.sahan.birdspacex.domain.usecase.GetLaunchDetailUseCase
import com.sahan.birdspacex.domain.util.DispatcherProvider
import com.sahan.birdspacex.domain.util.DomainResult
import com.sahan.birdspacex.domain.util.NetworkError
import com.sahan.birdspacex.presentation.mvi.SpaceAction
import com.sahan.birdspacex.presentation.mvi.SpaceEvent
import com.sahan.birdspacex.presentation.mvi.UiError
import kotlinx.coroutines.launch

class LaunchDetailViewModel(
    private val getLaunchDetailUseCase: GetLaunchDetailUseCase,
    dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
) : SpaceViewModel(dispatcherProvider = dispatcherProvider) {

    private var currentLaunchId: String? = null

    override fun onAction(action: SpaceAction) {
        when (action) {
            is SpaceAction.LoadLaunchDetail -> {
                currentLaunchId = action.launchId
                loadDetail(action.launchId)
            }

            SpaceAction.Retry -> {
                currentLaunchId?.let(::loadDetail)
            }

            is SpaceAction.OpenExternalLink -> {
                launch(dispatcher = dispatcherProvider.main) {
                    emitEvent(SpaceEvent.OpenUrl(action.url))
                }
            }

            SpaceAction.LoadLaunches,
            is SpaceAction.OnLaunchClick,
            is SpaceAction.Refresh,
                -> Unit
        }
    }

    private fun loadDetail(launchId: String) {
        launch(dispatcher = dispatcherProvider.io) {
            updateState {
                it.copy(isLoading = true, error = null)
            }

            when (val result = getLaunchDetailUseCase(launchId)) {
                is DomainResult.Success -> {
                    updateState {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            selectedLaunchDetail = result.data,
                            error = null,
                        )
                    }

                    if (result.fromCache) {
                        emitEvent(SpaceEvent.ShowSnackbar("Cache data gösteriliyor.."))
                    }
                }

                is DomainResult.Error -> {
                    updateState {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = UiError(
                                title = "Launch detayı yüklenirken bir hata oluştu",
                                message = result.error.toReadableMessage(),
                            ),
                        )
                    }
                }
            }
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
