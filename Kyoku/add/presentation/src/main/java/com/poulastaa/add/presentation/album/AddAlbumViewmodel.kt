package com.poulastaa.add.presentation.album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.poulastaa.add.domain.repository.AddAlbumRepository
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.UiText
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
internal class AddAlbumViewmodel @Inject constructor(
    private val repo: AddAlbumRepository,
) : ViewModel() {
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
        if (_state.value.isSavingAlbums && action !is AddAlbumUiAction.OnSearchQueryChange) {
            viewModelScope.launch {
                _uiEvent.send(
                    AddAlbumUiEvent.EmitToast(
                        UiText.StringResource(
                            R.string.saving_album_wait
                        )
                    )
                )
            }

            return
        }

        when (action) {
            is AddAlbumUiAction.OnSearchQueryChange -> {
                _state.update {
                    it.copy(
                        query = it.query.copy(
                            value = action.query
                        )
                    )
                }

                loadAlbumJob?.cancel()
                loadAlbumJob = loadAlbum()
            }

            is AddAlbumUiAction.OnAlbumClick -> when (action.clickType) {
                AddAlbumUiAction.ClickType.EDIT -> {
                    when (_state.value.selectedAlbums.any { it.id == action.album.id }) {
                        true -> {
                            _state.update {
                                it.copy(
                                    selectedAlbums = it.selectedAlbums.filterNot { it.id == action.album.id },
                                    isSelectedAlbumOpen = if (it.selectedAlbums.size < 2) false else it.isSelectedAlbumOpen
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
                                    selectedAlbums = _state.value.selectedAlbums + action.album,
                                )
                            }.also {
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
                }

                AddAlbumUiAction.ClickType.VIEW -> _state.update {
                    it.copy(
                        viewAlbumScreenState = ViewAlbumUiState(
                            isVisible = true,
                            album = action.album
                        ),
                        isSelectedAlbumOpen = false
                    )
                }
            }

            AddAlbumUiAction.OnViewCancel -> _state.update {
                it.copy(
                    viewAlbumScreenState = ViewAlbumUiState()
                )
            }

            is AddAlbumUiAction.OnSearchFilterTypeChange -> {
                if (action.filterType == _state.value.searchFilterType) return

                _state.update {
                    it.copy(
                        searchFilterType = action.filterType
                    )
                }

                loadAlbumJob?.cancel()
                loadAlbumJob = loadAlbum()
            }

            AddAlbumUiAction.OnSaveClick -> viewModelScope.launch {
                when (val result = repo.saveAlbums(_state.value.selectedAlbums.map { it.id })) {
                    is Result.Error -> when (result.error) {
                        DataError.Network.NO_INTERNET -> _uiEvent.send(
                            AddAlbumUiEvent.EmitToast(
                                UiText.StringResource(
                                    R.string.error_no_internet
                                )
                            )
                        )

                        else -> _uiEvent.send(
                            AddAlbumUiEvent.EmitToast(
                                UiText.StringResource(
                                    R.string.error_something_went_wrong
                                )
                            )
                        )
                    }

                    is Result.Success -> {
                        _uiEvent.send(
                            AddAlbumUiEvent.EmitToast(
                                UiText.DynamicString("${_state.value.selectedAlbums.size} ${R.string.album_saved}")
                            )
                        )

                        val selectedAlbums = _state.value.selectedAlbums

                        _album.update {
                            it.filter {
                                it !in selectedAlbums
                            }
                        }

                        _state.update {
                            it.copy(
                                selectedAlbums = emptyList(),
                            )
                        }
                    }
                }

                _state.update {
                    it.copy(
                        isSavingAlbums = false
                    )
                }
            }

            AddAlbumUiAction.OnClearAllDialogToggle -> {
                if (_state.value.isClearAllDialogOpen) _album.update {
                    it.map {
                        it.copy(
                            isSelected = false
                        )
                    }
                }

                _state.update {
                    it.copy(
                        isClearAllDialogOpen = it.isClearAllDialogOpen.not()
                    )
                }
            }

            AddAlbumUiAction.OnSaveCancelClick -> _state.update {
                it.copy(
                    isClearAllDialogOpen = false,
                    selectedAlbums = emptyList(),
                )
            }

            AddAlbumUiAction.OnViewSelectedToggle -> _state.update {
                it.copy(
                    isSelectedAlbumOpen = it.isSelectedAlbumOpen.not(),
                )
            }
        }
    }

    private fun loadAlbum() = viewModelScope.launch {
        _album.update {
            PagingData.empty()
        }

        repo.loadAlbum(
            query = _state.value.query.value.trim(),
            filterType = _state.value.searchFilterType.toDtoAddAlbumFilterType()
        ).cachedIn(viewModelScope).collectLatest { page ->
            _state.update {
                it.copy(
                    loadingType = LoadingType.Content
                )
            }

            _album.update {
                page.map {
                    val album = it.toUiAlbum()

                    if (_state.value.selectedAlbums.isEmpty()
                        || _state.value.selectedAlbums.firstOrNull { it.id == album.id } == null
                    ) album
                    else album.copy(isSelected = true)
                }
            }
        }
    }
}