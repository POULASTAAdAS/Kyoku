package com.poulastaa.play.presentation.view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.view.ViewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ViewViewModel @Inject constructor(
    private val ds: DataStoreRepository,
    private val repo: ViewRepository
) : ViewModel() {
    var state by mutableStateOf(ViewUiState())
        private set

    fun loadData(
        id: Long,
        type: ViewDataType
    ) {
        viewModelScope.launch {
            state = when (type) {
                ViewDataType.PLAYLIST -> state.copy(
                    data = repo.getPlaylistOnId(id).toViewUiSong()
                )

                ViewDataType.ALBUM -> state.copy(
                    data = repo.getAlbumOnId(id).toViewUiAlbum()
                )

                ViewDataType.FEV -> state.copy(
                    data = repo.getFev().toOtherData()
                )

                ViewDataType.ARTIST_MIX -> state.copy(
                    data = repo.getArtistMix().toOtherData()
                )

                ViewDataType.POPULAR_MIX -> state.copy(
                    data = repo.getPopularMix().toOtherData()
                )

                ViewDataType.OLD_MIX -> state.copy(
                    data = repo.getOldMix().toOtherData()
                )
            }

            withContext(Dispatchers.Main) {
                state = if (state.data.name.isBlank()) state.copy(
                    loadingState = ViewLoadingState.ERROR
                ) else state.copy(
                    loadingState = ViewLoadingState.LOADED
                )
            }
        }
        readHeader()
    }

    fun onEvent(event: ViewUiEvent) {
        when (event) {
            ViewUiEvent.OnPlayClick -> {

            }

            ViewUiEvent.OnShuffleClick -> {

            }

            ViewUiEvent.OnDownloadClick -> {

            }

            is ViewUiEvent.OnSongClick -> {

            }

            is ViewUiEvent.OnMoveClick -> {

            }

            is ViewUiEvent.OnThreeDotClick -> {

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