package com.poulastaa.play.presentation.explore_artist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreArtistViewModel @Inject constructor(
    private val ds: DataStoreRepository
) : ViewModel() {
    var state by mutableStateOf(ExploreArtistUiState())
        private set


    private val _uiEvent = Channel<ExploreArtistUiAction>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        readHeader()
    }

    fun loadData(artistId: Long) {

    }

    fun onEvent(event: ExploreArtistUiEvent) {

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