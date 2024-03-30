package com.poulastaa.kyoku.presentation.screen.song_view.artist

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.poulastaa.kyoku.connectivity.NetworkObserver
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent
import com.poulastaa.kyoku.data.model.screens.song_view.ArtistAllUiState
import com.poulastaa.kyoku.domain.usecase.ArtistPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistAllViewModel @Inject constructor(
    private val connectivity: NetworkObserver,
    private val pager: ArtistPagingSource
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

    var state by mutableStateOf(ArtistAllUiState())
        private set

    init {
        viewModelScope.launch {
            pager.load(name = "Arijit Singh")

            Pager(
                config = PagingConfig(
                    pageSize = 20,
                    enablePlaceholders = false
                )
            ) {
                pager
            }.flow.map {
                it.map {
                    Log.d("data", it.toString())
                }
            }.cachedIn(viewModelScope)
        }
    }
}