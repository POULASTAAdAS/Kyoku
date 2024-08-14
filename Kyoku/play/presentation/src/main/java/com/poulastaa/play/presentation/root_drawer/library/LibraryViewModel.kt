package com.poulastaa.play.presentation.root_drawer.library

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.library.LibraryRepository
import com.poulastaa.play.presentation.root_drawer.library.model.LibraryViewType
import com.poulastaa.play.presentation.root_drawer.toUiAlbum
import com.poulastaa.play.presentation.root_drawer.toUiPrevPlaylist
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
class LibraryViewModel @Inject constructor(
    private val ds: DataStoreRepository,
    private val repo: LibraryRepository,
) : ViewModel() {
    var state by mutableStateOf(LibraryUiState())
        private set

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

    private val _uiEvent = Channel<LibraryUiAction>()
    val uiEvent = _uiEvent.receiveAsFlow()


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

            is LibraryUiEvent.OnItemLongClick -> {
                viewModelScope.launch {
                    when (event.type) {
                        LibraryBottomSheetLongClickType.ALBUM -> {
                            val pinnedDef = async { }

                            val album = state.data.album.firstOrNull {
                                it.id == event.id
                            } ?: return@launch

                            state = state.copy(
                                libraryBottomSheet = state.libraryBottomSheet.copy(
                                    isPinned = false, // todo
                                    id = event.id,
                                    type = event.type,
                                    title = album.name,
                                    urls = listOf(album.coverImage)
                                )
                            )
                        }

                        LibraryBottomSheetLongClickType.ARTIST -> {
                            val pinnedDef = async { }

                            val artist = state.data.artist.firstOrNull {
                                it.id == event.id
                            } ?: return@launch

                            state = state.copy(
                                libraryBottomSheet = state.libraryBottomSheet.copy(
                                    isPinned = false, // todo
                                    id = event.id,
                                    type = event.type,
                                    title = artist.name,
                                    urls = listOf(artist.coverImageUrl)
                                )
                            )
                        }

                        LibraryBottomSheetLongClickType.PLAYLIST -> {
                            val pinnedDef = async { }

                            val playlist = state.data.playlist.firstOrNull {
                                it.id == event.id
                            } ?: return@launch

                            state = state.copy(
                                libraryBottomSheet = state.libraryBottomSheet.copy(
                                    isPinned = false, // todo
                                    id = event.id,
                                    type = event.type,
                                    title = playlist.name,
                                    urls = playlist.urls
                                )
                            )
                        }

                        LibraryBottomSheetLongClickType.FAVOURITE -> {
                            val pinnedDef = async { }

                            state = state.copy(
                                libraryBottomSheet = state.libraryBottomSheet.copy(
                                    isPinned = false, // todo
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

            else -> Unit
        }
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

    private fun populate() {
        readFavourite()
        readPlaylist()
        readAlbum()
        readArtist()
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
}