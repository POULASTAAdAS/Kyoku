package com.poulastaa.play.presentation.add_new_artist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.presentation.ui.SnackBarType
import com.poulastaa.core.presentation.ui.SnackBarUiState
import com.poulastaa.core.presentation.ui.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNewArtistVewModel @Inject constructor(
    private val ds: DataStoreRepository
) : ViewModel() {
    var state by mutableStateOf(AddNewArtistUiState())
        private set

    private val _uiEvent = Channel<AddNewArtistUiAction>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _artist: MutableStateFlow<PagingData<AddArtistUiArtist>> =
        MutableStateFlow(PagingData.empty())
    var artist = _artist.asStateFlow()
        private set

    private var loadDataJob: Job? = null
    private var showSnackBarJob: Job? = null

    private var saveArtistIdList = mutableListOf<Long>()

    init {
        readHeader()
        loadDataJob?.cancel()
        loadDataJob = loadData()
    }

    fun onEvent(event: AddArtistUiEvent) {
        if (state.isMakingApiCall) return

        when (event) {
            is AddArtistUiEvent.OnArtistClick -> {
                viewModelScope.launch {
                    _uiEvent.send(
                        AddNewArtistUiAction.Navigate(
                            AddNewArtistOtherScreen.ViewArtist(
                                event.artistId
                            )
                        )
                    )
                }
            }

            AddArtistUiEvent.OnSearchToggle -> {
                state = state.copy(
                    isSearchEnabled = state.isSearchEnabled.not()
                )

                state = state.copy(searchQuery = "")

                loadDataJob?.cancel()
                loadDataJob = loadData()
            }

            is AddArtistUiEvent.OnSearchQueryChange -> {
                if (state.searchQuery == event.query.trim()) return

                state = state.copy(
                    searchQuery = event.query.trim()
                )

                loadDataJob?.cancel()
                loadDataJob = loadData()
            }

            is AddArtistUiEvent.OnFilterTypeChange -> {
                if (state.type == event.type) return

                state = state.copy(
                    type = event.type
                )

                loadDataJob?.cancel()
                loadDataJob = loadData()
            }

            AddArtistUiEvent.OnMassSelectToggle -> {
                if (state.isMassSelectEnabled) saveArtistIdList.clear()

                _artist.value = _artist.value.map {
                    it.copy(isSelected = false)
                }

                state = state.copy(
                    isMassSelectEnabled = state.isMassSelectEnabled.not()
                )
            }

            is AddArtistUiEvent.OnCheckChange -> {
                _artist.value = _artist.value.map {
                    if (it.id == event.id) it.copy(isSelected = event.status)
                    else it
                }

                if (event.status) saveArtistIdList.add(event.id)
                else saveArtistIdList.remove(event.id)
            }

            AddArtistUiEvent.OnSaveClick -> {
                state = state.copy(
                    isMakingApiCall = true
                )

                saveArtist(saveArtistIdList.toList())
            }
        }
    }

    private fun readHeader() {
        viewModelScope.launch {
            ds.readTokenOrCookie().collectLatest {
                state = state.copy(
                    header = it
                )
            }
        }
    }

    private fun loadData() = viewModelScope.launch {

    }

    private fun showSnackBar(type: SnackBarType, message: UiText) = viewModelScope.launch {
        state = state.copy(
            toast = SnackBarUiState(
                type = type,
                isVisible = true,
                message = message
            )
        )

        delay(4000)

        state = state.copy(
            toast = SnackBarUiState()
        )
    }

    private fun saveArtist(idList: List<Long>) {
        if (idList.isEmpty()) {
            state = state.copy(
                isMakingApiCall = false
            )

            return
        }

        viewModelScope.launch {

        }
    }
}