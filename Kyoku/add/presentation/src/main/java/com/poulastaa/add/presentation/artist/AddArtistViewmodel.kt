package com.poulastaa.add.presentation.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
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
internal class AddArtistViewmodel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(AddArtistUiState())
    val state = _state.onStart {

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AddArtistUiState()
    )

    private val _uiEvent = Channel<AddArtistUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _artist: MutableStateFlow<PagingData<UiArtist>> =
        MutableStateFlow(PagingData.empty())
    val artist = _artist.asStateFlow()

    fun onAction(action: AddArtistUiAction) {
        if (_state.value.isSaving) return

        when (action) {
            is AddArtistUiAction.OnSearchQueryChange -> _state.update {
                it.copy(
                    query = action.query
                )
            }

            is AddArtistUiAction.OnFilterTypeChange -> {
                if (action.filterType == _state.value.searchFilterType) return

                _state.update {
                    it.copy(
                        searchFilterType = action.filterType
                    )
                }
            }

            is AddArtistUiAction.OnItemClick -> {
                when (_state.value.selectedArtist.contains(action.artist)) {
                    true -> {
                        _state.update {
                            it.copy(
                                selectedArtist = it.selectedArtist.filterNot { it.id == action.artist.id }
                            )
                        }

                        _artist.update {
                            it.map {
                                if (it.id == action.artist.id) it.copy(isSelected = false)
                                else it
                            }
                        }
                    }

                    false -> {
                        _state.update {
                            it.copy(
                                selectedArtist = it.selectedArtist.filterNot { it.id == action.artist.id }
                            )
                        }

                        _artist.update {
                            it.map {
                                if (it.id == action.artist.id) it.copy(isSelected = true)
                                else it
                            }
                        }
                    }
                }
            }

            AddArtistUiAction.OnSaveClick -> {
                _state.update {
                    it.copy(
                        isSaving = true
                    )
                }
            }

            AddArtistUiAction.OnCancelSaveClick -> {
                _state.update {
                    it.copy(
                        selectedArtist = emptyList()
                    )
                }
            }
        }
    }

    private fun loadArtist() = viewModelScope.launch {
        
    }
}