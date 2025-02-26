package com.poulastaa.main.presentation.library

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.repository.DatastoreRepository
import com.poulastaa.main.domain.repository.LibraryRepository
import com.poulastaa.main.presentation.home.mapper.toUiSavedItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@HiltViewModel
internal class LibraryViewmodel @Inject constructor(
    private val ds: DatastoreRepository,
    private val repo: LibraryRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(LibraryUiState())
    val state = _state.onStart {
        stopLoadingAfterFiveSecIfNoData()
        updateLibraryViewType()
        loadPlaylist()
        loadArtist()
        loadAlbum()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = LibraryUiState()
    )

    fun onAction(action: LibraryUiAction) {
        Log.d("action", action.toString())

        when (action) {
            is LibraryUiAction.OnFilterTypeToggle -> {
                if (action.type == _state.value.filterType) return

                _state.update {
                    it.copy(
                        filterType = action.type
                    )
                }
            }

            is LibraryUiAction.OnViewTypeToggle -> {
                viewModelScope.launch {
                    ds.storeLibraryViewType(
                        when (_state.value.viewType) {
                            UiLibraryViewType.GRID -> false
                            UiLibraryViewType.LIST -> true
                        }
                    )
                }
            }

            is LibraryUiAction.OnEditSavedItemTypeClick -> {}
            is LibraryUiAction.OnItemClick -> {}
        }
    }

    private fun stopLoadingAfterFiveSecIfNoData() {
        viewModelScope.launch {
            delay(5.0.toDuration(DurationUnit.SECONDS))

            if (_state.value.canShowUi.not()) {
                _state.update {
                    it.copy(
                        noSavedData = true
                    )
                }
            }
        }
    }

    private fun updateLibraryViewType() {
        viewModelScope.launch {
            ds.readLibraryViewType().collectLatest {
                _state.update { uiState ->
                    uiState.copy(
                        viewType = when (it) {
                            true -> UiLibraryViewType.GRID
                            false -> UiLibraryViewType.LIST
                        }
                    )
                }
            }
        }
    }

    private fun loadPlaylist() {
        viewModelScope.launch {
            repo.loadSavedPlaylist().collectLatest {
                _state.update { state ->
                    state.copy(
                        playlist = it.map { it.toUiSavedItem() }
                    )
                }
            }
        }
    }

    private fun loadArtist() {
        viewModelScope.launch {
            repo.loadSavedArtist().collectLatest {
                _state.update { state ->
                    state.copy(
                        artist = it.map { it.toUiSavedItem() }
                    )
                }
            }
        }
    }

    private fun loadAlbum() {
        viewModelScope.launch {
            repo.loadSavedAlbum().collectLatest {
                _state.update { state ->
                    state.copy(
                        album = it.map { it.toUiSavedItem() }
                    )
                }
            }
        }
    }
}