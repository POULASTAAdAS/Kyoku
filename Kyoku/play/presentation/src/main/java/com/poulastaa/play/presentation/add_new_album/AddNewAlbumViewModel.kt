package com.poulastaa.play.presentation.add_new_album

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.repository.new_album.NewAlbumRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNewAlbumViewModel @Inject constructor(
    private val ds: DataStoreRepository,
    private val repo: NewAlbumRepository
) : ViewModel() {
    var state by mutableStateOf(AddAlbumUiState())
        private set

    private val _uiEvent = Channel<AddNewAlbumUiAction>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        readHeader()
    }

    fun onEvent(event: AddAlbumUiEvent) {
        when (event) {
            is AddAlbumUiEvent.OnAlbumClick -> {

            }

            AddAlbumUiEvent.OnSearchToggle -> {
                state = state.copy(
                    isSearchEnabled = state.isSearchEnabled.not()
                )
            }

            is AddAlbumUiEvent.OnSearchQueryChange -> {
                state = state.copy(
                    searchQuery = event.query
                )
            }

            else -> {

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
}