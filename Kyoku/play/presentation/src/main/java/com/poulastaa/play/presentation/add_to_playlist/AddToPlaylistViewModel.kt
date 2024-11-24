package com.poulastaa.play.presentation.add_to_playlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.repository.add_to_playlist.AddToPlaylistRepository
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.ui.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddToPlaylistViewModel @Inject constructor(
    private val ds: DataStoreRepository,
    private val repo: AddToPlaylistRepository,
) : ViewModel() {
    var state by mutableStateOf(AddToPlaylistUiState())
        private set

    private val _uiEvent = Channel<AddToPlaylistUiAction>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        readHeader()
    }

    fun loadData(songId: Long) {
        if (songId == -1L) {
            viewModelScope.launch {
                _uiEvent.send(
                    AddToPlaylistUiAction.EmitToast(
                        UiText.StringResource(R.string.error_something_went_wrong)
                    )
                )
            }

            return
        }

        state = state.copy(
            songId = songId
        )

        loadFavourite(songId)
        loadPlaylist(songId)
    }

    fun onEvent(event: AddToPlaylistUiEvent) {
        when (event) {
            AddToPlaylistUiEvent.EnableSearch -> {
                state = state.copy(
                    isSearchEnable = true,
                    oldPlaylistData = state.playlistData
                )
            }

            AddToPlaylistUiEvent.CancelSearch -> {
                state = state.copy(
                    query = "",
                    isSearchEnable = false,
                    playlistData = state.oldPlaylistData
                )
            }

            is AddToPlaylistUiEvent.OnSearchQueryChange -> {
                val query = event.query

                state = state.copy(
                    query = event.query
                )

                state = if (state.query.isNotBlank()) state.copy(
                    playlistData = state.oldPlaylistData.mapNotNull {
                        if (it.playlist.name.contains(query, ignoreCase = true)) it else null
                    }
                ) else state.copy(
                    playlistData = state.oldPlaylistData
                )
            }

            AddToPlaylistUiEvent.AddNewPlaylist -> {
                state = state.copy(
                    addNewPlaylistBottomSheetState = state.addNewPlaylistBottomSheetState.copy(
                        isOpen = true
                    )
                )
            }

            AddToPlaylistUiEvent.OnFevToggle -> {
                val new = state.favouriteData.selectStatus.new.not()
                val old = state.favouriteData.selectStatus.old

                state = state.copy(
                    favouriteData = state.favouriteData.copy(
                        selectStatus = state.favouriteData.selectStatus.copy(
                            new = new
                        ),
                        totalSongs = if (old) {
                            if (!new) state.favouriteData.totalSongs.dec()
                            else state.favouriteData.totalSongs.inc()
                        } else {
                            if (new) state.favouriteData.totalSongs.inc()
                            else state.favouriteData.totalSongs.dec()
                        }
                    )
                )
            }

            is AddToPlaylistUiEvent.OnPlaylistClick -> {
                val playlist =
                    state.playlistData.first { it.playlist.id == event.playlistId }
                val oldData =
                    state.oldPlaylistData.first { it.playlist.id == event.playlistId }

                viewModelScope.launch(Dispatchers.Default) {
                    val old = async {
                        state.oldPlaylistData.map {
                            if (it.playlist.id == event.playlistId) {
                                it.copy(
                                    selectStatus = toggleSelectStatus(it.selectStatus),
                                    totalSongs = adjustTotalSongs(
                                        it.totalSongs,
                                        oldData.selectStatus
                                    )
                                )
                            } else it
                        }
                    }

                    val new = async {
                        state.playlistData.map {
                            if (it.playlist.id == event.playlistId) {
                                it.copy(
                                    selectStatus = toggleSelectStatus(it.selectStatus),
                                    totalSongs = adjustTotalSongs(
                                        it.totalSongs,
                                        playlist.selectStatus
                                    )
                                )
                            } else it
                        }
                    }


                    state = state.copy(
                        oldPlaylistData = old.await(),
                        playlistData = new.await()
                    )
                }
            }

            AddToPlaylistUiEvent.OnSaveClick -> {
                if (state.isMakingApiCall) return

                state = state.copy(
                    isMakingApiCall = true
                )

                viewModelScope.launch(Dispatchers.IO) {
                    val saveSong = repo.saveSong(state.songId)

                    if (saveSong is Result.Error) {
                        when (saveSong.error) {
                            DataError.Network.NO_INTERNET -> {
                                _uiEvent.send(
                                    AddToPlaylistUiAction.EmitToast(
                                        UiText.StringResource(R.string.error_no_internet)
                                    )
                                )
                            }

                            else -> {
                                _uiEvent.send(
                                    AddToPlaylistUiAction.EmitToast(
                                        UiText.StringResource(R.string.error_something_went_wrong)
                                    )
                                )
                            }
                        }

                        state = state.copy(
                            isMakingApiCall = false
                        )

                        return@launch
                    }

                    val fevDef = async {
                        if (state.favouriteData.selectStatus.old != state.favouriteData.selectStatus.new)
                            if (state.favouriteData.selectStatus.new) repo.addSongToFavourite(state.songId)
                            else repo.removeSongFromFavourite(state.songId)
                    }

                    val playlistDef = async {
                        state.playlistData.mapNotNull {
                            if (it.selectStatus.old != it.selectStatus.new) it.playlist.id to it.selectStatus.new else null
                        }.toMap().let {
                            repo.editPlaylist(state.songId, it)
                        }
                    }


                    fevDef.await()
                    val playlist = playlistDef.await()


                    if (playlist is Result.Error) {
                        when (playlist.error) {
                            DataError.Network.NO_INTERNET -> {
                                _uiEvent.send(
                                    AddToPlaylistUiAction.EmitToast(
                                        UiText.StringResource(R.string.error_no_internet)
                                    )
                                )
                            }

                            else -> {
                                _uiEvent.send(
                                    AddToPlaylistUiAction.EmitToast(
                                        UiText.StringResource(R.string.error_something_went_wrong)
                                    )
                                )
                            }
                        }
                    }

                    state = state.copy(
                        isMakingApiCall = false
                    )

                    _uiEvent.send(
                        AddToPlaylistUiAction.EmitToast(
                            UiText.StringResource(R.string.playlist_updated)
                        )
                    )

                    _uiEvent.send(AddToPlaylistUiAction.NavigateBack)
                }
            }

            is AddToPlaylistUiEvent.AddNewPlaylistUiEvent -> {
                when (event) {
                    is AddToPlaylistUiEvent.AddNewPlaylistUiEvent.OnNameChange -> {
                        state = state.copy(
                            addNewPlaylistBottomSheetState = state.addNewPlaylistBottomSheetState.copy(
                                name = event.name
                            )
                        )
                    }

                    AddToPlaylistUiEvent.AddNewPlaylistUiEvent.OnCancelClick -> {
                        state = state.copy(
                            addNewPlaylistBottomSheetState = AddNewPlaylistBottomSheetUiState()
                        )
                    }

                    AddToPlaylistUiEvent.AddNewPlaylistUiEvent.OnSaveClick -> {
                        if (!validatePlaylistName(state.addNewPlaylistBottomSheetState.name)) return

                        state = state.copy(
                            addNewPlaylistBottomSheetState = state.addNewPlaylistBottomSheetState.copy(
                                isMakingApiCall = true
                            )
                        )

                        viewModelScope.launch {
                            val result = repo.createPlaylist(
                                songId = state.songId,
                                name = state.addNewPlaylistBottomSheetState.name
                            )

                            when (result) {
                                is Result.Error -> {
                                    when (result.error) {
                                        DataError.Network.NO_INTERNET -> _uiEvent.send(
                                            AddToPlaylistUiAction.EmitToast(
                                                UiText.StringResource(R.string.error_no_internet)
                                            )
                                        )

                                        else -> _uiEvent.send(
                                            AddToPlaylistUiAction.EmitToast(
                                                UiText.StringResource(R.string.error_something_went_wrong)
                                            )
                                        )
                                    }
                                }

                                is Result.Success -> {
                                    val data = repo.getPlaylistData(state.songId)
                                        .map { it.toPlaylistData() }

                                    withContext(Dispatchers.Default) {
                                        val playlist = async {
                                            data.map { entry ->
                                                entry.copy(
                                                    selectStatus = state.playlistData.firstOrNull { it.playlist.id == entry.playlist.id }?.selectStatus
                                                        ?: entry.selectStatus
                                                )
                                            }
                                        }

                                        val oldPlaylist = async {
                                            data.map { entry ->
                                                entry.copy(
                                                    selectStatus = state.oldPlaylistData.firstOrNull { it.playlist.id == entry.playlist.id }?.selectStatus
                                                        ?: entry.selectStatus
                                                )
                                            }
                                        }

                                        state = state.copy(
                                            playlistData = playlist.await(),
                                            oldPlaylistData = oldPlaylist.await()
                                        )
                                    }

                                    _uiEvent.send(
                                        AddToPlaylistUiAction.EmitToast(
                                            UiText.StringResource(R.string.playlist_created)
                                        )
                                    )
                                }
                            }
                        }

                        state = state.copy(
                            addNewPlaylistBottomSheetState = AddNewPlaylistBottomSheetUiState()
                        )
                    }
                }
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

    private fun loadFavourite(songId: Long) {
        viewModelScope.launch {
            val statusDef = async { repo.checkIfSongInFev(songId) }
            val totalDef = async { repo.getTotalSongsInFev() }

            val status = statusDef.await()
            val total = totalDef.await()

            state = state.copy(
                favouriteData = state.favouriteData.copy(
                    selectStatus = state.favouriteData.selectStatus.copy(
                        old = status,
                        new = status
                    ),
                    totalSongs = total
                )
            )
        }
    }

    private fun loadPlaylist(songId: Long) = viewModelScope.launch {
        val data = repo.getPlaylistData(songId).map { it.toPlaylistData() }

        state = state.copy(
            playlistData = data,
            oldPlaylistData = data
        )
    }

    private fun toggleSelectStatus(
        currentStatus: UiSelectStatus,
    ): UiSelectStatus = currentStatus.copy(new = currentStatus.new.not())

    private fun adjustTotalSongs(
        currentTotal: Int,
        selectStatus: UiSelectStatus,
    ) = if (selectStatus.old) {
        if (selectStatus.new) currentTotal.dec() else currentTotal.inc()
    } else {
        if (!selectStatus.new) currentTotal.inc() else currentTotal.dec()
    }


    private fun validatePlaylistName(name: String): Boolean {
        if (name.trim().isEmpty()) {
            state = state.copy(
                addNewPlaylistBottomSheetState = state.addNewPlaylistBottomSheetState.copy(
                    errorMessage = UiText.StringResource(R.string.error_empty_playlist_name),
                    isValidName = false
                )
            )

            return false
        }

        if (name.length < 4) {
            state = state.copy(
                addNewPlaylistBottomSheetState = state.addNewPlaylistBottomSheetState.copy(
                    errorMessage = UiText.StringResource(R.string.error_short_playlist_name),
                    isValidName = false
                )
            )

            return false
        }

        return true
    }
}