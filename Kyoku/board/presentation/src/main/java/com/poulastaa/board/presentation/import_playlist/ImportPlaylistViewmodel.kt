package com.poulastaa.board.presentation.import_playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.board.domain.import_playlist.ImportPlaylistRepository
import com.poulastaa.board.presentation.R
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.presentation.SnackBarEventManager
import com.poulastaa.core.presentation.SnackBarEventType
import com.poulastaa.core.presentation.SnackBarUiEvent
import com.poulastaa.core.presentation.designsystem.TextProp
import com.poulastaa.core.presentation.designsystem.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes
import com.poulastaa.core.presentation.ui.R as CoreR

const val SPOTIFY_URL = "https://open.spotify.com/playlist/"
private typealias PLAYLIST_ID = String

@HiltViewModel
internal class ImportPlaylistViewmodel @Inject constructor(
    val eventManager: SnackBarEventManager,
    private val repo: ImportPlaylistRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(ImportPlaylistUiState())
    val state = _state.onStart {
        loadInitialData()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5.minutes.inWholeSeconds),
        initialValue = ImportPlaylistUiState()
    )

    private val _uiEvent = Channel<ImportPlaylistUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onAction(action: ImportPlaylistUiAction) {
        if ((_state.value.isMakingApiCall ||
                    _state.value.isLoading ||
                    _state.value.isSkipping) &&
            action !is ImportPlaylistUiAction.OnItemStateToggle
        ) return

        when (action) {
            is ImportPlaylistUiAction.OnLinkChange -> _state.update { it.copy(link = TextProp(value = action.link)) }
            ImportPlaylistUiAction.OnImportClick -> {
                val playlistId = _state.value.link.value.trim().extractLink()
                if (playlistId == null) {
                    _state.update {
                        it.copy(
                            link = it.link.copy(
                                isErr = false,
                                errText = UiText.StringResource(R.string.invalid_link)
                            )
                        )
                    }

                    return
                }

                _state.update { it.copy(isMakingApiCall = true) }
                viewModelScope.launch {
                    when (val result = repo.requestPlaylist(playlistId)) {
                        is Result.Error -> when (result.error) {
                            DataError.Network.NO_INTERNET -> eventManager.showEvent(
                                SnackBarUiEvent(
                                    eventType = SnackBarEventType.ERROR,
                                    message = UiText.StringResource(CoreR.string.please_check_internet_connection)
                                )
                            )

                            else -> eventManager.showEvent(
                                SnackBarUiEvent(
                                    eventType = SnackBarEventType.ERROR,
                                    message = UiText.StringResource(CoreR.string.something_went_wrong_try_again)
                                )
                            )
                        }

                        is Result.Success -> eventManager.showEvent(
                            SnackBarUiEvent(
                                eventType = SnackBarEventType.SUCCESS,
                                message = UiText.StringResource(
                                    CoreR.string.success
                                )
                            )
                        )
                    }

                    _state.update { it.copy(isMakingApiCall = false) }
                }
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
                _uiEvent.send(ImportPlaylistUiEvent.NavigateToSelectBDate)
            }
        }
    }

    private fun loadInitialData() = viewModelScope.launch {
        repo.loadAllPlaylist().collectLatest { list ->
            _state.update {
                it.copy(
                    data = list.map { dto -> dto.toUiPrevPlaylist() }
                )
            }

            // give ui time to load
            delay(300)

            if (_state.value.isLoading.not()) _state.update { it.copy(isLoading = false) }
        }
    }

    private fun String.extractLink(): PLAYLIST_ID? =
        if (this.startsWith(SPOTIFY_URL) && this.contains("?si="))
            this.removePrefix(SPOTIFY_URL).split("?si=")[0]
        else null
}