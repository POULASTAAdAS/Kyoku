package com.poulastaa.play.presentation.view_edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.repository.view_edit.ViewEditRepository
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewEditViewModel @Inject constructor(
    private val ds: DataStoreRepository,
    private val repo: ViewEditRepository
) : ViewModel() {
    var state by mutableStateOf(ViewEditUiState())
        private set

    private var loadDataJob: Job? = null

    fun init(info: ViewEditUiInfo) {
        state = state.copy(
            data = state.data.copy(
                info = info
            )
        )

        loadDataJob?.cancel()
        loadDataJob = loadData()
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

                deleteSong(event.songId)
            }
        }
    }

    private fun loadData() = viewModelScope.launch {
        repo.getSongs(
            playlistId = state.data.info.id,
            type = state.data.info.type
        ).collectLatest {
            state = state.copy(
                data = state.data.copy(
                    songs = it.map { song ->
                        song.toViewEditUiSong()
                    }
                )
            )
        }
    }

    private fun deleteSong(songId: Long) = viewModelScope.launch {
        when (val result = repo.deleteSong(state.data.info.id, songId)) {
            is Result.Error -> {
                when (result.error) {
                    DataError.Network.NO_INTERNET -> {

                    }

                    else -> {

                    }
                }
            }

            is Result.Success -> {
                state = state.copy(
                    data = state.data.copy(
                        songs = state.data.songs.filter { it.id != songId }
                    )
                )
            }
        }
    }

    private fun readHeader() = viewModelScope.launch {
        ds.readTokenOrCookie().collectLatest {
            state = state.copy(
                header = it
            )
        }
    }
}