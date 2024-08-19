package com.poulastaa.play.presentation.root_drawer.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.ExploreType
import com.poulastaa.core.domain.home.HomeRepository
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.ui.UiText
import com.poulastaa.play.presentation.OtherScreens
import com.poulastaa.play.presentation.add_as_playlist.PlaylistBottomSheetUiState
import com.poulastaa.play.presentation.root_drawer.home.mapper.getCurrentTime
import com.poulastaa.play.presentation.root_drawer.home.mapper.getDayType
import com.poulastaa.play.presentation.root_drawer.home.mapper.toUiHomeData
import com.poulastaa.play.presentation.root_drawer.home.model.HomeItemBottomSheetUiState
import com.poulastaa.play.presentation.root_drawer.model.HomeItemClickType
import com.poulastaa.play.presentation.root_drawer.toUiAlbum
import com.poulastaa.play.presentation.root_drawer.toUiPrevPlaylist
import com.poulastaa.play.presentation.view.ViewDataType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
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

    private val _uiEvent = Channel<HomeUiAction>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        setHomeTopBarTitle()
        readHeader()
        loadData()
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.OnItemClick -> {
                viewModelScope.launch(Dispatchers.IO) {
                    when (event.itemClickType) {
                        HomeItemClickType.SAVED_PLAYLIST -> {
                            _uiEvent.send(
                                HomeUiAction.Navigate(
                                    OtherScreens.View(
                                        id = event.id ?: -1L,
                                        type = ViewDataType.PLAYLIST
                                    )
                                )
                            )
                        }

                        HomeItemClickType.SAVED_ALBUM -> {
                            _uiEvent.send(
                                HomeUiAction.Navigate(
                                    OtherScreens.View(
                                        id = event.id ?: -1L,
                                        type = ViewDataType.ALBUM
                                    )
                                )
                            )
                        }

                        HomeItemClickType.POPULAR_SONG_MIX -> {
                            _uiEvent.send(
                                HomeUiAction.Navigate(
                                    OtherScreens.View(
                                        id = event.id ?: -1L,
                                        type = ViewDataType.POPULAR_MIX
                                    )
                                )
                            )
                        }

                        HomeItemClickType.OLD_GEM -> {
                            _uiEvent.send(
                                HomeUiAction.Navigate(
                                    OtherScreens.View(
                                        id = event.id ?: -1L,
                                        type = ViewDataType.OLD_MIX
                                    )
                                )
                            )
                        }

                        HomeItemClickType.FAVOURITE_ARTIST_MIX -> {
                            _uiEvent.send(
                                HomeUiAction.Navigate(
                                    OtherScreens.View(
                                        id = event.id ?: -1L,
                                        type = ViewDataType.ARTIST_MIX
                                    )
                                )
                            )
                        }

                        HomeItemClickType.SUGGEST_ALBUM -> {
                            _uiEvent.send(
                                HomeUiAction.Navigate(
                                    OtherScreens.View(
                                        id = event.id ?: -1L,
                                        type = ViewDataType.ALBUM
                                    )
                                )
                            )
                        }

                        else -> Unit

//                        HomeItemClickType.SUGGEST_ARTIST -> TODO()
//                        HomeItemClickType.POPULAR_ARTIST -> TODO()
//                        HomeItemClickType.HISTORY_SONG -> TODO()
//                        HomeItemClickType.SUGGEST_ARTIST_SONG -> TODO()
//                        HomeItemClickType.NON -> TODO()
                    }
                }
            }

            is HomeUiEvent.OnItemLongClick -> {
                when (event.itemClickType) {
                    HomeItemClickType.SAVED_PLAYLIST -> Unit
                    HomeItemClickType.SAVED_ALBUM -> Unit


                    HomeItemClickType.POPULAR_SONG_MIX -> {
                        state = state.copy(
                            itemBottomSheetUiState = HomeItemBottomSheetUiState(
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
                            itemBottomSheetUiState = HomeItemBottomSheetUiState(
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
                            itemBottomSheetUiState = HomeItemBottomSheetUiState(
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
                            itemBottomSheetUiState = HomeItemBottomSheetUiState(
                                isOpen = true,
                            )
                        )

                        viewModelScope.launch(Dispatchers.IO) {
                            val isArtistInLibrary = homeRepo.isArtistIsInLibrary(artist.id)

                            state = state.copy(
                                itemBottomSheetUiState = state.itemBottomSheetUiState.copy(
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
                            itemBottomSheetUiState = HomeItemBottomSheetUiState(
                                isOpen = true,
                            )
                        )

                        viewModelScope.launch(Dispatchers.IO) {
                            val isAlbumInLibrary = homeRepo.isAlbumInLibrary(album.id)

                            state = state.copy(
                                itemBottomSheetUiState = state.itemBottomSheetUiState.copy(
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
                            itemBottomSheetUiState = HomeItemBottomSheetUiState(
                                isOpen = true,
                            )
                        )

                        viewModelScope.launch(Dispatchers.IO) {
                            val isSongInFavouriteDef = async { homeRepo.isSongInFavourite(song.id) }

                            val isSongInFavourite = isSongInFavouriteDef.await()

                            state = state.copy(
                                itemBottomSheetUiState = state.itemBottomSheetUiState.copy(
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

            is HomeUiEvent.ItemBottomSheetUiEvent -> {
                when (event) {
                    HomeUiEvent.ItemBottomSheetUiEvent.Cancel -> Unit

                    // recommendations
                    HomeUiEvent.ItemBottomSheetUiEvent.AddAsPlaylistPopularSongMix -> {
                        state = state.copy(
                            playlistBottomSheetUiState = PlaylistBottomSheetUiState(
                                isOpen = true,
                                exploreType = ExploreType.POPULAR
                            )
                        )
                    }


                    HomeUiEvent.ItemBottomSheetUiEvent.AddAsPlaylistOldGem -> {
                        state = state.copy(
                            playlistBottomSheetUiState = PlaylistBottomSheetUiState(
                                isOpen = true,
                                exploreType = ExploreType.OLD_GEM
                            )
                        )
                    }

                    HomeUiEvent.ItemBottomSheetUiEvent.AddAsFavouriteArtistMix -> {
                        state = state.copy(
                            playlistBottomSheetUiState = PlaylistBottomSheetUiState(
                                isOpen = true,
                                exploreType = ExploreType.ARTIST_MIX
                            )
                        )
                    }

                    // artist
                    is HomeUiEvent.ItemBottomSheetUiEvent.FollowArtist -> {
                        viewModelScope.launch {
                            if (homeRepo.followArtist(event.id)) _uiEvent.send(
                                HomeUiAction.EmitToast(
                                    UiText.StringResource(R.string.artist_added_to_library)
                                )
                            ) else _uiEvent.send(
                                HomeUiAction.EmitToast(
                                    UiText.StringResource(R.string.error_something_went_wrong)
                                )
                            )
                        }
                    }

                    is HomeUiEvent.ItemBottomSheetUiEvent.UnFollowArtist -> {
                        viewModelScope.launch {
                            if (homeRepo.unFollowArtist(event.id)) _uiEvent.send(
                                HomeUiAction.EmitToast(
                                    UiText.StringResource(R.string.artist_removed_from_library)
                                )
                            ) else _uiEvent.send(
                                HomeUiAction.EmitToast(
                                    UiText.StringResource(R.string.error_something_went_wrong)
                                )
                            )
                        }
                    }

                    // album
                    is HomeUiEvent.ItemBottomSheetUiEvent.SaveAlbum -> {
                        viewModelScope.launch {
                            if (homeRepo.saveAlbum(event.id)) _uiEvent.send(
                                HomeUiAction.EmitToast(
                                    UiText.StringResource(R.string.album_added_to_library)
                                )
                            ) else _uiEvent.send(
                                HomeUiAction.EmitToast(
                                    UiText.StringResource(R.string.error_something_went_wrong)
                                )
                            )
                        }
                    }

                    is HomeUiEvent.ItemBottomSheetUiEvent.RemoveSavedAlbum -> {
                        viewModelScope.launch(Dispatchers.IO) {
                            when (homeRepo.removeAlbum(event.id)) {
                                true -> _uiEvent.send(
                                    HomeUiAction.EmitToast(
                                        UiText.StringResource(R.string.album_removed_from_favourite)
                                    )
                                )

                                false -> _uiEvent.send(
                                    HomeUiAction.EmitToast(
                                        UiText.StringResource(R.string.error_something_went_wrong)
                                    )
                                )
                            }
                        }
                    }

                    // song
                    is HomeUiEvent.ItemBottomSheetUiEvent.AddSongToFavourite -> {
                        viewModelScope.launch(Dispatchers.IO) {
                            if (homeRepo.insertIntoFavourite(event.id)) _uiEvent.send(
                                HomeUiAction.EmitToast(
                                    UiText.StringResource(R.string.song_added_to_favourite)
                                )
                            ) else _uiEvent.send(
                                HomeUiAction.EmitToast(
                                    UiText.StringResource(R.string.error_something_went_wrong)
                                )
                            )
                        }
                    }

                    is HomeUiEvent.ItemBottomSheetUiEvent.AddSongToPlaylist -> {
                        viewModelScope.launch {
                            _uiEvent.send(
                                HomeUiAction.Navigate(
                                    screen = OtherScreens.AddAsPlaylist(
                                        songId = event.id
                                    )
                                )
                            )
                        }
                    }

                    is HomeUiEvent.ItemBottomSheetUiEvent.RemoveSongToFavourite -> {
                        viewModelScope.launch(Dispatchers.IO) {
                            when (homeRepo.removeFromFavourite(event.id)) {
                                true -> _uiEvent.send(
                                    HomeUiAction.EmitToast(
                                        UiText.StringResource(R.string.song_removed_from_favourite)
                                    )
                                )

                                false -> _uiEvent.send(
                                    HomeUiAction.EmitToast(
                                        UiText.StringResource(R.string.error_something_went_wrong)
                                    )
                                )
                            }
                        }
                    }

                    else -> Unit
                }

                state = state.copy(
                    itemBottomSheetUiState = HomeItemBottomSheetUiState()
                )
            }

            is HomeUiEvent.PlaylistBottomSheetUiEvent -> {
                when (event) {
                    HomeUiEvent.PlaylistBottomSheetUiEvent.Cancel -> {
                        state = state.copy(
                            playlistBottomSheetUiState = PlaylistBottomSheetUiState()
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
            loadSavedAlbum()
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
                    result.toUiPrevPlaylist()
                }
            }.collectLatest {
                state = state.copy(
                    savedPlaylists = it,
                    isPlaylistLoaded = true
                )
            }
        }
    }

    private fun loadSavedAlbum() {
        viewModelScope.launch {
            homeRepo.loadSavedAlbum().map {
                it.map { result ->
                    result.toUiAlbum()
                }
            }.collectLatest {
                state = state.copy(
                    savedAlbums = it,
                    isAlbumLoaded = true
                )
            }
        }
    }
}