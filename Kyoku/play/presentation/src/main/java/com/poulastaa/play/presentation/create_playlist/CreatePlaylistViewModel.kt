package com.poulastaa.play.presentation.create_playlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.model.CreatePlaylistType
import com.poulastaa.core.domain.model.Song
import com.poulastaa.core.domain.repository.create_playlist.CreatePlaylistRepository
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.ui.UiText
import com.poulastaa.play.domain.DataLoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePlaylistViewModel @Inject constructor(
    private val ds: DataStoreRepository,
    private val repo: CreatePlaylistRepository
) : ViewModel() {
    var state by mutableStateOf(CreatePlaylistUiState())
        private set

    private val _uiEvent = Channel<CreatePlaylistUiAction>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var loadPagingDataJob: Job? = null

    private var generatedData: List<Pair<CreatePlaylistType, List<Song>>> = emptyList()

    fun init(playlistId: Long) {
        state = state.copy(
            playlistId = playlistId
        )
        readHeader()
        loadStaticData()

        loadPagingDataJob?.cancel()
        loadPagingDataJob = loadPagingData()
    }

    fun onEvent(event: CreatePlaylistUiEvent) {
        when (event) {
            is CreatePlaylistUiEvent.OnSongClick -> {

            }

            CreatePlaylistUiEvent.OnSearchToggle -> {

            }

            is CreatePlaylistUiEvent.OnSearchQueryChange -> {

            }
        }
    }

    private fun readHeader() {
        viewModelScope.launch {
            ds.readTokenOrCookie().collect {
                state = state.copy(header = it)
            }
        }
    }

    private fun loadStaticData() {
        viewModelScope.launch {
            when (val result = repo.getStaticData()) {
                is Result.Error -> {
                    when (result.error) {
                        DataError.Network.NO_INTERNET -> {
                            _uiEvent.send(
                                CreatePlaylistUiAction.EmitToast(
                                    UiText.StringResource(R.string.error_no_internet)
                                )
                            )
                        }

                        else -> {
                            _uiEvent.send(
                                CreatePlaylistUiAction.EmitToast(
                                    UiText.StringResource(R.string.error_something_went_wrong)
                                )
                            )
                        }
                    }
                }

                is Result.Success -> {
                    val list = result.data.mapNotNull {
                        if (it.second.isNotEmpty()) it.toCreatePlaylistData()
                        else null
                    } + CreatePlaylistData(
                        type = CreatePlaylistType.SEARCH,
                        list = emptyList()
                    )

                    state = state.copy(
                        generatedData = list.reversed()
                    )

                    generatedData = result.data
                }
            }

            state = if (state.generatedData.isEmpty()) state.copy(
                loadingState = DataLoadingState.ERROR
            ) else state.copy(
                loadingState = DataLoadingState.LOADED
            )
        }
    }

    private fun loadPagingData() = viewModelScope.launch {

    }
}