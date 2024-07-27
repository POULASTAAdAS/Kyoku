package com.poulastaa.play.presentation.root_drawer.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.home.HomeRepository
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.ui.UiText
import com.poulastaa.play.presentation.root_drawer.home.mapper.getCurrentTime
import com.poulastaa.play.presentation.root_drawer.home.mapper.getDayType
import com.poulastaa.play.presentation.root_drawer.home.mapper.toUiHomeData
import com.poulastaa.play.presentation.root_drawer.home.model.BottomSheetUiState
import com.poulastaa.play.presentation.root_drawer.model.HomeItemClickType
import com.poulastaa.play.presentation.root_drawer.toUiPlaylist
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val ds: DataStoreRepository,
    private val homeRepo: HomeRepository,
) : ViewModel() {
    var state by mutableStateOf(HomeUiState())
        private set

    init {
        setHomeTopBarTitle()
        readHeader()
        loadData()
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.OnItemClick -> {

            }

            is HomeUiEvent.OnItemLongClick -> {
                when (event.itemClickType) {
                    HomeItemClickType.SAVED_PLAYLIST -> Unit
                    HomeItemClickType.SAVED_ALBUM -> Unit
                    HomeItemClickType.POPULAR_SONG_MIX -> {
                        state = state.copy(
                            bottomSheetUiState = BottomSheetUiState(
                                isOpen = true,
                                isBottomSheetLoading = false,
                                title = UiText.StringResource(R.string.popular_song_mix)
                                    .asString(event.context),
                                urls = state.staticData.popularSongMix.map { it.coverImage },
                                itemType = event.itemClickType
                            )
                        )
                    }

                    HomeItemClickType.OLD_GEM -> {
                        state = state.copy(
                            bottomSheetUiState = BottomSheetUiState(
                                isOpen = true,
                                isBottomSheetLoading = false,
                                title = UiText.StringResource(R.string.old_gem)
                                    .asString(event.context),
                                urls = state.staticData.popularSongFromYourTime.map { it.coverImage },
                                itemType = event.itemClickType
                            )
                        )
                    }

                    HomeItemClickType.FAVOURITE_ARTIST_MIX -> {
                        state = state.copy(
                            bottomSheetUiState = BottomSheetUiState(
                                isOpen = true,
                                isBottomSheetLoading = false,
                                title = UiText.StringResource(R.string.favourite_artist_mix)
                                    .asString(event.context),
                                urls = state.staticData.favouriteArtistMix.map { it.coverImage },
                                itemType = event.itemClickType
                            )
                        )
                    }

                    HomeItemClickType.SUGGEST_ARTIST -> {
                        val artist = state.staticData.suggestedArtist.firstOrNull {
                            it.id == event.id
                        } ?: return

                        state = state.copy(
                            bottomSheetUiState = BottomSheetUiState(
                                isOpen = true,
                            )
                        )

                        viewModelScope.launch(Dispatchers.IO) {
                            val isArtistInLibrary = homeRepo.isArtistIsInLibrary(artist.id)

                            state = state.copy(
                                bottomSheetUiState = state.bottomSheetUiState.copy(
                                    isBottomSheetLoading = false,
                                    flag = isArtistInLibrary, // if artist is already is in library
                                    id = artist.id,
                                    title = artist.name,
                                    urls = listOf(artist.coverImageUrl),
                                    itemType = event.itemClickType
                                )
                            )
                        }
                    }

                    HomeItemClickType.POPULAR_ARTIST -> Unit
                    HomeItemClickType.SUGGEST_ALBUM -> {
                        val album = state.staticData.popularAlbum.firstOrNull {
                            it.id == event.id
                        } ?: return

                        state = state.copy(
                            bottomSheetUiState = BottomSheetUiState(
                                isOpen = true,
                            )
                        )

                        viewModelScope.launch(Dispatchers.IO) {
                            val isAlbumInLibrary = homeRepo.isAlbumInLibrary(album.id)

                            state = state.copy(
                                bottomSheetUiState = state.bottomSheetUiState.copy(
                                    isBottomSheetLoading = false,
                                    flag = isAlbumInLibrary, // if album saved in library
                                    id = album.id,
                                    title = album.name,
                                    urls = listOf(album.coverImage),
                                    itemType = event.itemClickType
                                )
                            )
                        }
                    }

                    HomeItemClickType.HISTORY_SONG -> Unit
                    HomeItemClickType.SUGGEST_ARTIST_SONG -> {
                        val song = state.staticData.popularArtistSong.firstOrNull {
                            it.artist.id == event.artistId
                        }?.listOfSong?.firstOrNull {
                            it.id == event.id
                        } ?: return

                        state = state.copy(
                            bottomSheetUiState = BottomSheetUiState(
                                isOpen = true,
                            )
                        )

                        viewModelScope.launch(Dispatchers.IO) {
                            val isSongInFavouriteDef = async { homeRepo.isSongInFavourite(song.id) }

                            val isSongInFavourite = isSongInFavouriteDef.await()

                            state = state.copy(
                                bottomSheetUiState = state.bottomSheetUiState.copy(
                                    isBottomSheetLoading = false,
                                    flag = false, // todo if anything playing false
                                    isQueue = false, // todo if is in playingQueue
                                    isInFavourite = isSongInFavourite, // if is in favourite
                                    id = song.id,
                                    title = song.title,
                                    urls = listOf(song.coverImage),
                                    itemType = event.itemClickType
                                )
                            )
                        }
                    }

                    else -> Unit
                }
            }

            is HomeUiEvent.BottomSheetUiEvent -> {
                when (event) {
                    HomeUiEvent.BottomSheetUiEvent.Cancel -> {
                        state = state.copy(
                            bottomSheetUiState = BottomSheetUiState()
                        )
                    }

                    else -> {
                        state = state.copy(
                            bottomSheetUiState = BottomSheetUiState()
                        )
                    }
                }
            }
        }
    }


    private fun setHomeTopBarTitle() {
        viewModelScope.launch {
            state = state.copy(
                heading = getCurrentTime(),
            )
        }
    }

    private fun readHeader() = viewModelScope.launch {
        ds.readTokenOrCookie().collectLatest {
            state = state.copy(
                header = it
            )
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            val newUserDef = async { homeRepo.isNewUser() }
            val populateDef = async { populate() }

            if (newUserDef.await()) return@launch loadNewUserData()
            populateDef.await()
            loadSavedPlaylist()
        }
    }

    private fun loadNewUserData() {
        viewModelScope.launch {
            val result = homeRepo.storeNewHomeResponse(
                dayType = getDayType()
            )

            when (result) {
                is Result.Error -> {
                    when (result.error) {
                        DataError.Network.NO_INTERNET -> {

                        }

                        else -> {

                        }
                    }
                }

                is Result.Success -> populate()
            }
        }
    }

    private suspend fun populate(): Unit = withContext(Dispatchers.IO) {
        val data = homeRepo.loadHomeData()

        withContext(Dispatchers.Main) { // canShowUi is not updating without thread change
            state = state.copy(
                isNewUser = false,
                isDataLoading = false,
                staticData = data.toUiHomeData()
            )
        }
    }

    private fun loadSavedPlaylist() {
        viewModelScope.launch {
            homeRepo.loadSavedPlaylist().map {
                it.map { result ->
                    result.toUiPlaylist()
                }
            }.collectLatest {
                state = state.copy(
                    savedPlaylists = it
                )
            }
        }
    }
}