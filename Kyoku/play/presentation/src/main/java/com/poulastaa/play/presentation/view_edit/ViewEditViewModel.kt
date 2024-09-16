package com.poulastaa.play.presentation.view_edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewEditViewModel @Inject constructor(
    private val ds: DataStoreRepository,
) : ViewModel() {
    var state by mutableStateOf(ViewEditUiState())
        private set

    private var deleteSongJob: Job? = null

    fun init(info: ViewEditUiInfo) {
        state = state.copy(
            data = state.data.copy(
                info = info
            )
        )

        readHeader()
    }

    fun onEvent(event: ViewEditUiEvent) {
        when (event) {
            ViewEditUiEvent.OnExploreClick -> {

            }

            is ViewEditUiEvent.OnDeleteClick -> {
                state = state.copy(
                    data = state.data.copy(
                        songs = state.data.songs.map {
                            if (it.id == event.songId) it.copy(isVisible = false)
                            else it
                        }
                    )
                )

                deleteSongJob?.cancel()
                deleteSongJob = deleteSong(event.songId)
            }
        }
    }

    private fun loadData() = viewModelScope.launch {

    }

    private fun deleteSong(songId: Long) = viewModelScope.launch {

    }

    private fun readHeader() = viewModelScope.launch {
        ds.readTokenOrCookie().collectLatest {
            state = state.copy(
                header = it
            )
        }
    }
}