package com.poulastaa.add.presentation.album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import com.poulastaa.add.presentation.components.OtherScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AddAlbumViewmodel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(AddAlbumUiState())
    val state = _state.onStart {
        loadAlbumJob?.cancel()
        loadAlbumJob = loadAlbum()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AddAlbumUiState()
    )

    private val _uiEvent = Channel<AddAlbumUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _album: MutableStateFlow<PagingData<UiAlbum>> = MutableStateFlow(PagingData.empty())
    val album = _album.asStateFlow()

    private var loadAlbumJob: Job? = null

    fun onAction(action: AddAlbumUiAction) {
        when (action) {
            is AddAlbumUiAction.OnSearchQueryChange -> _state.update {
                it.copy(
                    query = it.query.copy(
                        value = action.query
                    )
                )
            }

            is AddAlbumUiAction.OnAlbumClick -> when (action.clickType) {
                AddAlbumUiAction.ClickType.ADD -> {
                    when (_state.value.selectedAlbums.contains(action.album)) {
                        true -> {
                            _state.update {
                                it.copy(
                                    selectedAlbums = it.selectedAlbums.filterNot { it.id == action.album.id }
                                )
                            }

                            _album.update {
                                it.map {
                                    if (it.id == action.album.id) it.copy(
                                        isSelected = false
                                    ) else it
                                }
                            }
                        }

                        false -> {
                            _state.update {
                                it.copy(
                                    selectedAlbums = it.selectedAlbums + action.album
                                )
                            }

                            _album.update {
                                it.map {
                                    if (it.id == action.album.id) it.copy(
                                        isSelected = true
                                    ) else it
                                }
                            }
                        }
                    }
                }

                AddAlbumUiAction.ClickType.VIEW -> _state.update {
                    it.copy(
                        savedAlbumsScreenState = OtherScreenUiState(),
                        viewAlbumScreenState = OtherScreenUiState(
                            isVisible = true,
                            otherId = action.album.id
                        ),
                    )
                }
            }

            AddAlbumUiAction.OnSaveClick -> TODO()
        }
    }

    private fun loadAlbum() = viewModelScope.launch {

    }
}