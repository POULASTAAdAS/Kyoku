package com.poulastaa.kyoku.presentation.screen.edit_playlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.kyoku.connectivity.NetworkObserver
import com.poulastaa.kyoku.data.model.api.auth.AuthType
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent
import com.poulastaa.kyoku.data.model.screens.edit_playlist.EditPlaylistUiEvent
import com.poulastaa.kyoku.data.model.screens.edit_playlist.EditPlaylistUiState
import com.poulastaa.kyoku.data.repository.DatabaseRepositoryImpl
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditPlaylistViewModel @Inject constructor(
    private val connectivity: NetworkObserver,
    private val ds: DataStoreOperation,
    private val db: DatabaseRepositoryImpl
) : ViewModel() {
    private val network = mutableStateOf(NetworkObserver.STATUS.UNAVAILABLE)

    init {
        viewModelScope.launch {
            connectivity.observe().collect {
                network.value = it
                state = state.copy(
                    isInternetAvailable = it == NetworkObserver.STATUS.AVAILABLE,
                    isInternetError = false
                )
                if (!state.isInternetAvailable)
                    state = state.copy(
                        isInternetError = true
                    )
            }
        }
    }

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var state by mutableStateOf(EditPlaylistUiState())

    init {
        readAccessToken()
        readAuthType()
        loadData()
    }

    private fun readAccessToken() {
        viewModelScope.launch {
            ds.readTokenOrCookie().collect {
                state = state.copy(
                    headerValue = it
                )
            }
        }
    }

    private fun readAuthType() {
        viewModelScope.launch {
            val authType = ds.readAuthType().first()

            state = state.copy(
                isCookie = authType == AuthType.SESSION_AUTH.name
            )
        }
    }

    private fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            db.getAllPlaylist().collect {

            }
        }
    }

    fun onEvent(event: EditPlaylistUiEvent) {
        when (event) {
            is EditPlaylistUiEvent.EmitToast -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiEvent.send(UiEvent.ShowToast(event.message))
                }
            }

            EditPlaylistUiEvent.SomethingWentWrong -> {
                onEvent(EditPlaylistUiEvent.EmitToast("Opp's something went wrong"))
            }

            EditPlaylistUiEvent.SearchClick -> {
                state = state.copy(
                    isSearchEnable = true
                )
            }

            is EditPlaylistUiEvent.SearchText -> {
                state = state.copy(
                    searchText = event.text
                )

                // todo search job
            }

            EditPlaylistUiEvent.CancelSearch -> {
                state = state.copy(
                    isSearchEnable = false,
                    searchText = ""
                )
            }

            is EditPlaylistUiEvent.PlaylistClick -> {
                state = state.copy(
                    data = state.data.map {
                        if (it.id == event.id) it.copy(isSelected = !it.isSelected)
                        else it
                    }
                )
            }

            EditPlaylistUiEvent.DoneClick -> {

            }

            EditPlaylistUiEvent.NewPlaylistClick -> {

            }

            is EditPlaylistUiEvent.BottomSheetClick -> {
                when (event) {
                    is EditPlaylistUiEvent.BottomSheetClick.YesClick -> {

                    }

                    EditPlaylistUiEvent.BottomSheetClick.NoClick -> {

                    }
                }
            }
        }
    }
}