package com.sahan.birdspacex.presentation.viewmodel

import com.sahan.birdspacex.domain.DefaultDispatcherProvider
import com.sahan.birdspacex.domain.util.DispatcherProvider
import com.sahan.birdspacex.presentation.mvi.SpaceAction
import com.sahan.birdspacex.presentation.mvi.SpaceEvent
import com.sahan.birdspacex.presentation.mvi.SpaceUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

open class SpaceViewModel(
    dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
) {
    private val _state = MutableStateFlow(SpaceUiState())
    val state: StateFlow<SpaceUiState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<SpaceEvent>(extraBufferCapacity = 16)
    val events: SharedFlow<SpaceEvent> = _events.asSharedFlow()

    protected val viewModelScope = CoroutineScope(SupervisorJob() + dispatcherProvider.main)

    open fun onAction(action: SpaceAction) = Unit

    protected fun updateState(block: (SpaceUiState) -> SpaceUiState) {
        _state.update(block)
    }

    protected suspend fun emitEvent(event: SpaceEvent) {
        _events.emit(event)
    }

    open fun clear() {
        viewModelScope.cancel()
    }
}
