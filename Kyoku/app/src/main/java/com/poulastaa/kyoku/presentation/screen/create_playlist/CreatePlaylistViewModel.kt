package com.poulastaa.kyoku.presentation.screen.create_playlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.kyoku.connectivity.NetworkObserver
import com.poulastaa.kyoku.data.model.api.service.playlist.CreatePlaylistReq
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent
import com.poulastaa.kyoku.data.model.screens.create_playlist.CreatePlaylistUiEvent
import com.poulastaa.kyoku.data.model.screens.create_playlist.CreatePlaylistUiState
import com.poulastaa.kyoku.data.model.screens.home.HomeLongClickType
import com.poulastaa.kyoku.data.repository.DatabaseRepositoryImpl
import com.poulastaa.kyoku.domain.repository.ServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CreatePlaylistViewModel @Inject constructor(
    private val connectivity: NetworkObserver,
    private val db: DatabaseRepositoryImpl,
    private val api: ServiceRepository
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


    var state by mutableStateOf(CreatePlaylistUiState())

    fun loadData(
        id: Long,
        name: String,
        typeString: String,
    ) {
        val type = getType(typeString)

        state = state.copy(
            id = id,
            text = name,
            type = type
        )

        when (type) {
            HomeLongClickType.ARTIST_MIX -> viewModelScope.launch(Dispatchers.IO) {
                initialSetupForArtistMix()
            }

            HomeLongClickType.DAILY_MIX -> viewModelScope.launch(Dispatchers.IO) {
                initialSetUpForDailyMix()
            }

            HomeLongClickType.ALBUM_PREV -> state = state.copy(
                isLoading = false
            )

            else -> {

            }
        }
    }

    private fun getType(type: String) = when (type) {
        HomeLongClickType.ALBUM_PREV.name -> HomeLongClickType.ALBUM_PREV
        HomeLongClickType.ARTIST_MIX.name -> HomeLongClickType.ARTIST_MIX
        HomeLongClickType.DAILY_MIX.name -> HomeLongClickType.DAILY_MIX
        HomeLongClickType.HISTORY_SONG.name -> HomeLongClickType.HISTORY_SONG
        else -> HomeLongClickType.ARTIST_SONG
    }

    fun onEvent(event: CreatePlaylistUiEvent) {
        when (event) {
            is CreatePlaylistUiEvent.EmitToast -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiEvent.send(UiEvent.ShowToast(event.message))
                }
            }

            is CreatePlaylistUiEvent.EnterName -> {
                state = state.copy(
                    text = event.text
                )
            }

            CreatePlaylistUiEvent.SomethingWentWrong -> {
                onEvent(CreatePlaylistUiEvent.EmitToast("Opp's something went wrong"))
            }

            CreatePlaylistUiEvent.SaveClicked -> {
                viewModelScope.launch(Dispatchers.IO) {
                    when (state.type) {
                        HomeLongClickType.ARTIST_MIX -> {
                            if (state.songIdList.isEmpty()) return@launch initialSetupForArtistMix()

                            downloadPlaylistFromMix()
                        }

                        HomeLongClickType.DAILY_MIX -> {
                            if (state.songIdList.isEmpty()) return@launch initialSetUpForDailyMix()

                            downloadPlaylistFromMix()
                        }


                        HomeLongClickType.ALBUM_PREV -> {
                            if (network.value != NetworkObserver.STATUS.AVAILABLE) return@launch onEvent(
                                CreatePlaylistUiEvent.EmitToast("Please check your Internet connection")
                            )

                            if (!playlistNameValid()) return@launch onEvent(
                                CreatePlaylistUiEvent.EmitToast(
                                    message = "Playlist name can't start with number or spatial character"
                                )
                            )

                            if (db.isPlaylistNameDuplicate(state.text.trim())) return@launch onEvent(
                                CreatePlaylistUiEvent.EmitToast(
                                    message = "A playlist with this name already exists"
                                )
                            )


                            downloadPlaylistFromAlbum()
                        }

                        else -> Unit
                    }
                }
            }
        }
    }

    private suspend fun downloadPlaylistFromMix() {
        state = state.copy(
            isLoading = true
        )

        // name validation check
        if (!playlistNameValid()) {
            onEvent(
                CreatePlaylistUiEvent.EmitToast(
                    message = "Playlist name can't start with number or spatial character"
                )
            )

            state = state.copy(
                isLoading = false
            )

            return
        }

        // name duplicity check
        if (db.isPlaylistNameDuplicate(state.text.trim())) {
            onEvent(
                CreatePlaylistUiEvent.EmitToast(
                    message = "A playlist with this name already exists"
                )
            )

            state = state.copy(
                isLoading = false
            )

            return
        }


        val playlist = api.getPlaylistOnSongId(
            req = CreatePlaylistReq(
                name = state.text.trim(),
                listOfSongId = state.songIdList
            )
        )

        if (playlist.listOfSongs.isEmpty()) {
            onEvent(CreatePlaylistUiEvent.SomethingWentWrong)

            state = state.copy(
                isCriticalErr = true
            )

            return
        }

        db.insertIntoPlaylist(response = playlist)

        onEvent(CreatePlaylistUiEvent.EmitToast("${state.text} saved"))

        state = state.copy(
            isLoading = false,
            isCriticalErr = true // using field to exit out of screen on successful response
        )
    }

    private fun downloadPlaylistFromAlbum() {
        viewModelScope.launch(Dispatchers.IO) {
            val playlist = api.getPlaylistOnAlbumId(
                req = CreatePlaylistReq(
                    albumId = state.id,
                    name = state.text.trim()
                )
            )

            if (playlist.listOfSongs.isEmpty()) {
                onEvent(CreatePlaylistUiEvent.SomethingWentWrong)

                state = state.copy(
                    isCriticalErr = true
                )

                return@launch
            }

            db.insertIntoPlaylist(response = playlist)

            onEvent(CreatePlaylistUiEvent.EmitToast("${playlist.name} saved"))

            state = state.copy(
                isLoading = false,
                isCriticalErr = true // using field to exit out of screen on successful response
            )
        }
    }

    private suspend fun initialSetupForArtistMix() {
        withContext(Dispatchers.IO) {
            async { delay(500) }.await()

            // check if internet is available
            if (network.value != NetworkObserver.STATUS.AVAILABLE) {
                onEvent(CreatePlaylistUiEvent.EmitToast("Please check your Internet connection"))

                state = state.copy(
                    isLoading = false
                )

                return@withContext
            }

            if (db.isArtistMixEmpty()) {
                val artistMix = api.getArtistMix()

                if (artistMix.isEmpty()) {
                    onEvent(CreatePlaylistUiEvent.SomethingWentWrong)

                    state = state.copy(
                        isCriticalErr = true
                    )

                    return@withContext
                }

                db.insertIntoArtistMix(artistMix)

                state = state.copy(
                    isLoading = false,
                    songIdList = artistMix.map {
                        it.id
                    }
                )
            } else {
                state = state.copy(
                    songIdList = async { db.getSongIdListOfArtistMix() }.await(),
                    isLoading = false
                )
            }
        }
    }

    private suspend fun initialSetUpForDailyMix() {
        withContext(Dispatchers.IO) {
            async { delay(500) }.await()

            // check if internet is available
            if (network.value != NetworkObserver.STATUS.AVAILABLE) {
                onEvent(CreatePlaylistUiEvent.EmitToast("Please check your Internet connection"))

                state = state.copy(
                    isLoading = false
                )

                return@withContext
            }


            if (db.isDailyMixEmpty()) { // if daily mix table is empty
                // make api call
                val dailyMix = api.getDailyMix()

                if (dailyMix.isEmpty()) { // if empty return and exit out of screen
                    onEvent(CreatePlaylistUiEvent.SomethingWentWrong)

                    state = state.copy(
                        isCriticalErr = true
                    )

                    return@withContext
                }

                db.insertIntoDailyMix(dailyMix)

                state = state.copy(
                    isLoading = false,  // make save button clickable
                    songIdList = dailyMix.map {  // store listOf id to make api call
                        it.id
                    }
                )

            } else { // if daily mix table is not empty
                state = state.copy( // get dailyMix songs songId
                    songIdList = async { db.getSongIdListOfDailyMix() }.await(),
                    isLoading = false
                )
            }
        }
    }

    private fun playlistNameValid() =
        !(state.text.trim().matches(Regex("^\\W.*")) || // spatial char check
                state.text.trim().matches(Regex("^\\d.*"))) // number check
}