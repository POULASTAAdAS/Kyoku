package com.poulastaa.kyoku.presentation.screen.create_playlist

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.kyoku.connectivity.NetworkObserver
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent
import com.poulastaa.kyoku.data.model.screens.create_playlist.CreatePlaylistUiEvent
import com.poulastaa.kyoku.data.model.screens.create_playlist.CreatePlaylistUiState
import com.poulastaa.kyoku.data.model.screens.home.HomeLongClickType
import com.poulastaa.kyoku.data.repository.DatabaseRepositoryImpl
import com.poulastaa.kyoku.domain.repository.ServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
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

    private suspend fun isDailyMixDownloaded() = db.isDailyMixDownloaded()

    fun loadData(
        name: String,
        typeString: String,
    ) {
        state = state.copy(
            text = name
        )

        val type = getType(typeString)

        when (type) {
            HomeLongClickType.ARTIST_MIX -> {

            }

            HomeLongClickType.DAILY_MIX -> {
                viewModelScope.launch(Dispatchers.IO) {
                    initialSetUp()
                }
            }

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

    private suspend fun initialSetUp() {
        state = state.copy(
            isLoading = true
        )

        if (!isDailyMixDownloaded()) { // if daily mix table is empty

            // check if internet is available
            if (!state.isInternetAvailable) {
                onEvent(CreatePlaylistUiEvent.EmitToast("Please check your Internet connection"))

                state = state.copy(
                    isLoading = false
                )

                return
            }

            // make api call
            val dailyMix = api.getDailyMix().listOfSongs

            if (dailyMix.isEmpty()) { // if empty return and exit out of screen
                onEvent(CreatePlaylistUiEvent.SomethingWentWrong)

                state = state.copy(
                    isCriticalErr = true
                )

                return
            }

            db.insertIntoDailyMix(dailyMix)

            state = state.copy(
                isLoading = false,  // make save button clickable
                songIdList = dailyMix.map {  // store listOf id to make api call
                    it.id.toLong()
                }
            )

        } else { // if daily mix table is not empty
            state = state.copy( // get dailyMix songs songId
                songIdList = db.getSongIdListOfDailyMix(),
                isLoading = false
            )

            Log.d("called", "called ${state.isLoading}")
        }
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
                    if (state.songIdList.isEmpty()) return@launch initialSetUp()

                    // todo check if name has any conflict with other playlist name

                    // todo make api call to create playlist with listOf songId

//                    val playlist = api
//
//                    if (false) { // if api response is empty exiting from screen
//                        onEvent(CreatePlaylistUiEvent.EmitToast("Please check your Internet connection"))
//
//                        state = state.copy(
//                            isLoading = false
//                        )
//
//                        return@launch
//                    }
//
//                    db.insertIntoPlaylist(
//                        list = emptyList() // todo
//                    )

                    onEvent(CreatePlaylistUiEvent.EmitToast("${state.text} saved"))

                    state = state.copy(
                        isLoading = false,
                        isCriticalErr = true // using field to exit out of screen on successful response
                    )
                }
            }
        }
    }
}