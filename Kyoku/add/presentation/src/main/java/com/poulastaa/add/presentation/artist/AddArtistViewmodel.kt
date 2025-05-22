package com.poulastaa.add.presentation.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.poulastaa.add.domain.repository.AddArtistRepository
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AddArtistViewmodel @Inject constructor(
    private val repo: AddArtistRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(AddArtistUiState())
    val state = _state.onStart {
        loadArtistJob?.cancel()
        loadArtistJob = loadArtist()
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

    private var loadArtistJob: Job? = null

    fun onAction(action: AddArtistUiAction) {
        if (_state.value.isSaving) return

        when (action) {
            is AddArtistUiAction.OnSearchQueryChange -> _state.update {
                it.copy(
                    query = action.query
                )
            }.also {
                loadArtistJob?.cancel()
                loadArtistJob = loadArtist()
            }

            is AddArtistUiAction.OnFilterTypeChange -> {
                if (action.filterType == _state.value.searchFilterType) return

                _state.update {
                    it.copy(
                        searchFilterType = action.filterType
                    )
                }

                loadArtistJob?.cancel()
                loadArtistJob = loadArtist()
            }

            is AddArtistUiAction.OnItemClick -> {
                when (_state.value.selectedArtist.any { it.id == action.artist.id }) {
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
                                selectedArtist = it.selectedArtist + action.artist
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

            AddArtistUiAction.OnViewSelectedToggle -> _state.update {
                it.copy(
                    isSelectedBottomSheetOpen = it.isSelectedBottomSheetOpen.not()
                )
            }
        }
    }

    private fun loadArtist() = viewModelScope.launch {
        _artist.update {
            PagingData.empty()
        }

        repo.searchArtist(
            query = _state.value.query.trim(),
            filterType = _state.value.searchFilterType.toDtoAddArtistFilterType()
        ).cachedIn(viewModelScope).collectLatest { list ->
            _state.update {
                it.copy(
                    loadingType = LoadingType.Content
                )
            }

            _artist.update {
                list.map { it.toUiArtist() }
            }
        }
    }
}