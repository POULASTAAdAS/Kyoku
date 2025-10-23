package com.poulastaa.board.presentation.import_playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.presentation.SnackBarEventManager
import com.poulastaa.core.presentation.SnackBarEventType
import com.poulastaa.core.presentation.SnackBarUiEvent
import com.poulastaa.core.presentation.designsystem.TextProp
import com.poulastaa.core.presentation.designsystem.UiText
import com.poulastaa.core.presentation.ui.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
internal class ImportPlaylistViewmodel @Inject constructor(
    val eventManager: SnackBarEventManager,
) : ViewModel() {
    private val _state = MutableStateFlow(ImportPlaylistUiState())
    val state = _state.onStart {
        viewModelScope.launch {
            while (true) {
                eventManager.showEvent(
                    SnackBarUiEvent(
                        eventType = SnackBarEventType.ERROR,
                        message = UiText.StringResource(R.string.please_check_internet_connection)
                    )
                )

                delay(4.seconds)
            }
        }
        loadInitialData()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5.minutes.inWholeSeconds),
        initialValue = ImportPlaylistUiState()
    )

    private val _uiEvent = Channel<ImportPlaylistUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onAction(action: ImportPlaylistUiAction) {
        if (_state.value.isMakingApiCall || _state.value.isLoading || _state.value.isSkipping) return

        when (action) {
            is ImportPlaylistUiAction.OnLinkChange -> _state.update { it.copy(link = TextProp(value = action.link)) }
            ImportPlaylistUiAction.OnImportClick -> {
                _state.update { it.copy(isMakingApiCall = true) }

                // simulate error

            }

            is ImportPlaylistUiAction.OnItemStateToggle -> _state.update {
                it.copy(
                    data = it.data.map { item ->
                        if (item.id == action.id) item.copy(isExpanded = item.isExpanded.not())
                        else item
                    }
                )
            }

            ImportPlaylistUiAction.OnSkipClick -> viewModelScope.launch {
                _uiEvent.send(
                    ImportPlaylistUiEvent.NavigateToSelectBDate
                )
            }
        }
    }

    private fun loadInitialData() = viewModelScope.launch {
        delay(2_000)

        _state.update { it.copy(isLoading = false) }
    }
}