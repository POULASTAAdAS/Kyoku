package com.poulastaa.play.presentation.root_drawer.library

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.LibraryDataType
import com.poulastaa.core.domain.model.PinnedType
import com.poulastaa.core.domain.repository.library.LibraryRepository
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.ui.SnackBarType
import com.poulastaa.core.presentation.ui.SnackBarUiState
import com.poulastaa.core.presentation.ui.UiText
import com.poulastaa.play.presentation.root_drawer.library.model.LibraryViewType
import com.poulastaa.play.presentation.root_drawer.toUiAlbum
import com.poulastaa.play.presentation.root_drawer.toUiPrevPlaylist
import com.poulastaa.play.presentation.view.components.ViewDataType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val ds: DataStoreRepository,
    private val repo: LibraryRepository,
) : ViewModel() {
    var state by mutableStateOf(LibraryUiState())
        private set

    private val _uiEvent = Channel<LibraryUiAction>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var showToastJob: Job? = null

    init {
        readAuthHeader()
        readLibraryViewType()
        populate()
    }

    fun changeGridSizeIfExpanded() {
        state = state.copy(grid = 5)
    }

    fun revertGridSize() {
        if (state.grid == 3) return
        state = state.copy(grid = 3)
    }


    fun onEvent(event: LibraryUiEvent) {
        when (event) {
            LibraryUiEvent.OnSearchClick -> {

            }

            is LibraryUiEvent.ToggleFilterType -> {
                state = state.copy(
                    filterType = event.type
                )
            }

            LibraryUiEvent.ToggleView -> {
                state = state.copy(
                    viewType = if (LibraryViewType.LIST == state.viewType) LibraryViewType.GRID
                    else LibraryViewType.LIST
                )
            }

            is LibraryUiEvent.OnClick -> {
                viewModelScope.launch {
                    when (event) {
                        LibraryUiEvent.OnClick.PinnedHeader -> {

                        }

                        LibraryUiEvent.OnClick.AlbumHeader -> {

                        }

                        is LibraryUiEvent.OnClick.Album -> {
                            _uiEvent.send(
                                LibraryUiAction.Navigate(
                                    LibraryOtherScreen.View(
                                        id = event.id,
                                        type = ViewDataType.ALBUM
                                    )
                                )
                            )
                        }

                        LibraryUiEvent.OnClick.ArtistHeader -> {

                        }

                        is LibraryUiEvent.OnClick.Artist -> {
                            _uiEvent.send(
                                LibraryUiAction.Navigate(
                                    LibraryOtherScreen.ViewArtist(
                                        id = event.id
                                    )
                                )
                            )
                        }

                        LibraryUiEvent.OnClick.Favourite -> {
                            _uiEvent.send(
                                LibraryUiAction.Navigate(
                                    LibraryOtherScreen.View(
                                        type = ViewDataType.FEV
                                    )
                                )
                            )
                        }

                        is LibraryUiEvent.OnClick.Playlist -> {
                            _uiEvent.send(
                                LibraryUiAction.Navigate(
                                    LibraryOtherScreen.View(
                                        id = event.id,
                                        type = ViewDataType.PLAYLIST
                                    )
                                )
                            )
                        }

                        LibraryUiEvent.OnClick.PlaylistHeader -> {

                        }
                    }
                }
            }

            is LibraryUiEvent.OnItemLongClick -> {
                viewModelScope.launch {
                    when (event.type) {
                        LibraryBottomSheetLongClickType.ALBUM -> {
                            val pinnedDef = async { repo.checkIfPinned(event.id, PinnedType.ALBUM) }

                            val album = state.data.album.firstOrNull {
                                it.id == event.id
                            } ?: return@launch

                            state = state.copy(
                                libraryBottomSheet = state.libraryBottomSheet.copy(
                                    isPinned = pinnedDef.await(),
                                    id = event.id,
                                    type = event.type,
                                    title = album.name,
                                    urls = listOf(album.coverImage)
                                )
                            )
                        }

                        LibraryBottomSheetLongClickType.ARTIST -> {
                            val pinnedDef =
                                async { repo.checkIfPinned(event.id, PinnedType.ARTIST) }

                            val artist = state.data.artist.firstOrNull {
                                it.id == event.id
                            } ?: return@launch

                            state = state.copy(
                                libraryBottomSheet = state.libraryBottomSheet.copy(
                                    isPinned = pinnedDef.await(),
                                    id = event.id,
                                    type = event.type,
                                    title = artist.name,
                                    urls = listOf(artist.coverImageUrl)
                                )
                            )
                        }

                        LibraryBottomSheetLongClickType.PLAYLIST -> {
                            val pinnedDef =
                                async { repo.checkIfPinned(event.id, PinnedType.PLAYLIST) }

                            val playlist = state.data.playlist.firstOrNull {
                                it.id == event.id
                            } ?: return@launch

                            state = state.copy(
                                libraryBottomSheet = state.libraryBottomSheet.copy(
                                    isPinned = pinnedDef.await(),
                                    id = event.id,
                                    type = event.type,
                                    title = playlist.name,
                                    urls = playlist.urls
                                )
                            )
                        }

                        LibraryBottomSheetLongClickType.FAVOURITE -> {
                            state = state.copy(
                                libraryBottomSheet = state.libraryBottomSheet.copy(
                                    isPinned = state.data.isFevPinned,
                                    id = event.id,
                                    type = event.type,
                                    title = "Favourite",
                                )
                            )
                        }

                        else -> return@launch
                    }

                    state = state.copy(
                        libraryBottomSheet = state.libraryBottomSheet.copy(
                            isOpen = true
                        )
                    )
                }
            }

            LibraryUiEvent.OnItemBottomSheetCancel -> {
                state = state.copy(
                    libraryBottomSheet = LibraryBottomSheetUiState()
                )
            }

            is LibraryUiEvent.BottomSheetUiEvent -> {
                state = state.copy(
                    libraryBottomSheet = LibraryBottomSheetUiState()
                )

                when (event) {
                    is LibraryUiEvent.BottomSheetUiEvent.Favourite ->
                        handleFavouriteBottomSheetUiEvent(event)

                    is LibraryUiEvent.BottomSheetUiEvent.Playlist ->
                        handlePlaylistBottomSheetUiEvent(event)

                    is LibraryUiEvent.BottomSheetUiEvent.Album ->
                        handleAlbumBottomSheetUiEvent(event)

                    is LibraryUiEvent.BottomSheetUiEvent.Artist ->
                        handleArtistBottomSheetUiEvent(event)
                }
            }
        }
    }

    private fun handleFavouriteBottomSheetUiEvent(event: LibraryUiEvent.BottomSheetUiEvent.Favourite) {
        when (event) {
            is LibraryUiEvent.BottomSheetUiEvent.Favourite.Play -> {

            }

            is LibraryUiEvent.BottomSheetUiEvent.Favourite.Pin -> {
                viewModelScope.launch {
                    val result = repo.pinData(event.id, LibraryDataType.FAVOURITE)
                    handleResult(result, R.string.favourite_pinned)
                }
            }

            is LibraryUiEvent.BottomSheetUiEvent.Favourite.UnPin -> {
                viewModelScope.launch {
                    val result = repo.unPinData(event.id, LibraryDataType.FAVOURITE)
                    handleResult(result, R.string.favourite_un_pined)
                }
            }

            is LibraryUiEvent.BottomSheetUiEvent.Favourite.View -> {
                viewModelScope.launch {
                    _uiEvent.send(
                        LibraryUiAction.Navigate(
                            LibraryOtherScreen.View(
                                type = ViewDataType.FEV
                            )
                        )
                    )
                }
            }

            is LibraryUiEvent.BottomSheetUiEvent.Favourite.Download -> {

            }
        }
    }

    private fun handleAlbumBottomSheetUiEvent(event: LibraryUiEvent.BottomSheetUiEvent.Album) {
        when (event) {
            is LibraryUiEvent.BottomSheetUiEvent.Album.Play -> {

            }

            is LibraryUiEvent.BottomSheetUiEvent.Album.Pin -> {
                viewModelScope.launch {
                    val result = repo.pinData(event.id, LibraryDataType.ALBUM)
                    handleResult(result, R.string.album_pinned)
                }
            }

            is LibraryUiEvent.BottomSheetUiEvent.Album.UnPin -> {
                viewModelScope.launch {
                    val result = repo.unPinData(event.id, LibraryDataType.ALBUM)
                    handleResult(result, R.string.album_un_pined)
                }
            }

            is LibraryUiEvent.BottomSheetUiEvent.Album.View -> {
                viewModelScope.launch {
                    _uiEvent.send(
                        LibraryUiAction.Navigate(
                            LibraryOtherScreen.View(
                                id = event.id,
                                type = ViewDataType.ALBUM
                            )
                        )
                    )
                }
            }

            is LibraryUiEvent.BottomSheetUiEvent.Album.Remove -> {
                viewModelScope.launch {
                    val result = repo.deleteSavedData(event.id, LibraryDataType.ALBUM)
                    handleResult(result, R.string.album_deleted)
                }
            }

            is LibraryUiEvent.BottomSheetUiEvent.Album.Download -> {

            }
        }
    }

    private fun handlePlaylistBottomSheetUiEvent(event: LibraryUiEvent.BottomSheetUiEvent.Playlist) {
        when (event) {
            is LibraryUiEvent.BottomSheetUiEvent.Playlist.Play -> {

            }

            is LibraryUiEvent.BottomSheetUiEvent.Playlist.Pin -> {
                viewModelScope.launch {
                    val result = repo.pinData(event.id, LibraryDataType.PLAYLIST)
                    handleResult(result, R.string.playlist_pinned)
                }
            }

            is LibraryUiEvent.BottomSheetUiEvent.Playlist.UnPin -> {
                viewModelScope.launch {
                    val result = repo.unPinData(event.id, LibraryDataType.PLAYLIST)
                    handleResult(result, R.string.playlist_un_pinned)
                }
            }

            is LibraryUiEvent.BottomSheetUiEvent.Playlist.View -> {
                viewModelScope.launch {
                    _uiEvent.send(
                        LibraryUiAction.Navigate(
                            LibraryOtherScreen.View(
                                id = event.id,
                                type = ViewDataType.PLAYLIST
                            )
                        )
                    )
                }
            }

            is LibraryUiEvent.BottomSheetUiEvent.Playlist.Download -> {

            }

            is LibraryUiEvent.BottomSheetUiEvent.Playlist.Remove -> {
                viewModelScope.launch {
                    val result = repo.deleteSavedData(event.id, LibraryDataType.PLAYLIST)
                    handleResult(result, R.string.playlist_deleted)
                }
            }
        }
    }

    private fun handleArtistBottomSheetUiEvent(event: LibraryUiEvent.BottomSheetUiEvent.Artist) {
        when (event) {
            is LibraryUiEvent.BottomSheetUiEvent.Artist.Pin -> {
                viewModelScope.launch {
                    val result = repo.pinData(event.id, LibraryDataType.ARTIST)
                    handleResult(result, R.string.artist_pinned)
                }
            }

            is LibraryUiEvent.BottomSheetUiEvent.Artist.UnPin -> {
                viewModelScope.launch {
                    val result = repo.unPinData(event.id, LibraryDataType.ARTIST)
                    handleResult(result, R.string.artist_un_pinned)
                }
            }

            is LibraryUiEvent.BottomSheetUiEvent.Artist.View -> {
                viewModelScope.launch {
                    _uiEvent.send(
                        LibraryUiAction.Navigate(
                            LibraryOtherScreen.ViewArtist(
                                id = event.id
                            )
                        )
                    )
                }
            }

            is LibraryUiEvent.BottomSheetUiEvent.Artist.UnFollow -> {
                viewModelScope.launch {
                    val result = repo.deleteSavedData(event.id, LibraryDataType.ARTIST)
                    handleResult(result, R.string.artist_un_followed)
                }
            }
        }
    }


    private fun populate() {
        getPinnedData()
        readFevPinnedState()
        readFavourite()
        readPlaylist()
        readAlbum()
        readArtist()
    }

    private fun readAuthHeader() {
        viewModelScope.launch {
            ds.readTokenOrCookie().collectLatest {
                state = state.copy(
                    header = it
                )
            }
        }
    }

    private fun readLibraryViewType() {
        viewModelScope.launch {
            val type = if (ds.readLibraryViewType()) LibraryViewType.GRID else LibraryViewType.LIST

            withContext(Dispatchers.Main) {
                state = state.copy(
                    viewType = type,
                    viewTypeReading = false
                )
            }
        }
    }

    private fun readFavourite() {
        viewModelScope.launch {
            val status = repo.isFavourite()

            state = state.copy(
                data = state.data.copy(
                    isFavouriteEntry = status
                )
            )
        }
    }

    private fun readPlaylist() {
        viewModelScope.launch {
            repo.getPlaylist().collectLatest { list ->
                state = state.copy(
                    data = state.data.copy(
                        playlist = list.map {
                            it.toUiPrevPlaylist()
                        }
                    )
                )
            }
        }
    }

    private fun readAlbum() {
        viewModelScope.launch {
            repo.getAlbum().collectLatest { list ->
                state = state.copy(
                    data = state.data.copy(
                        album = list.map {
                            it.toUiAlbum()
                        }
                    )
                )
            }
        }
    }

    private fun readArtist() {
        viewModelScope.launch {
            repo.getArtist().collectLatest {
                state = state.copy(
                    data = state.data.copy(
                        artist = it.map { artist ->
                            artist.toUiArtist()
                        }
                    ),
                    isDataLoading = false
                )
            }
        }
    }

    private fun getPinnedData() {
        viewModelScope.launch {
            repo.getPinnedData().collectLatest {
                state = state.copy(
                    data = state.data.copy(
                        pinned = it.map { entry -> entry.toPinnedUiData() }
                    )
                )
            }
        }
    }

    private fun handleResult(
        result: EmptyResult<DataError.Network>,
        @StringRes messageId: Int
    ) {
        showToastJob?.cancel()

        when (result) {
            is Result.Error -> {
                when (result.error) {
                    DataError.Network.NO_INTERNET -> {
                        showToastJob = showToast(
                            SnackBarUiState(
                                isVisible = true,
                                message = UiText.StringResource(R.string.error_no_internet),
                                type = SnackBarType.ERROR
                            )
                        )
                    }

                    else -> {
                        showToastJob = showToast(
                            SnackBarUiState(
                                isVisible = true,
                                message = UiText.StringResource(R.string.error_something_went_wrong),
                                type = SnackBarType.ERROR
                            )
                        )
                    }
                }
            }

            is Result.Success -> {
                showToastJob = showToast(
                    SnackBarUiState(
                        isVisible = true,
                        message = UiText.StringResource(messageId),
                        type = SnackBarType.SUCCESS
                    )
                )
            }
        }
    }

    private fun showToast(toast: SnackBarUiState) = viewModelScope.launch {
        state = state.copy(
            toast = state.toast.copy(
                message = toast.message,
                type = toast.type
            )
        )

        state = state.copy(
            toast = state.toast.copy(
                isVisible = true
            )
        )

        delay(5_000) // 5's

        state = state.copy(
            toast = state.toast.copy(
                isVisible = false
            )
        )
    }

    private fun readFevPinnedState() {
        viewModelScope.launch {
            ds.isFevPinned().collectLatest {
                state = state.copy(
                    data = state.data.copy(
                        isFevPinned = it
                    )
                )
            }
        }
    }
}