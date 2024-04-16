package com.poulastaa.kyoku.presentation.screen.home_root.library

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.kyoku.connectivity.NetworkObserver
import com.poulastaa.kyoku.data.model.api.service.item.ItemOperation
import com.poulastaa.kyoku.data.model.api.service.item.ItemReq
import com.poulastaa.kyoku.data.model.api.service.pinned.IdType
import com.poulastaa.kyoku.data.model.api.service.pinned.PinnedOperation
import com.poulastaa.kyoku.data.model.api.service.pinned.PinnedReq
import com.poulastaa.kyoku.data.model.database.table.internal.InternalItemTable
import com.poulastaa.kyoku.data.model.database.table.internal.InternalPinnedTable
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent
import com.poulastaa.kyoku.data.model.screens.common.ItemsType
import com.poulastaa.kyoku.data.model.screens.common.UiAlbumPrev
import com.poulastaa.kyoku.data.model.screens.common.UiPlaylistPrev
import com.poulastaa.kyoku.data.model.screens.library.LibraryUiEvent
import com.poulastaa.kyoku.data.model.screens.library.LibraryUiState
import com.poulastaa.kyoku.data.model.screens.library.PinnedDataType
import com.poulastaa.kyoku.data.repository.DatabaseRepositoryImpl
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import com.poulastaa.kyoku.domain.repository.ServiceRepository
import com.poulastaa.kyoku.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.Random
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val connectivity: NetworkObserver,
    private val ds: DataStoreOperation,
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

    var state by mutableStateOf(LibraryUiState())
        private set

    fun loadData() {
        if (state.data.all.playlist.isEmpty() &&
            state.data.all.artist.isEmpty()
        ) {
            // read sort type
            viewModelScope.launch(Dispatchers.IO) {
                ds.readLibraryDataSortType().collect {
                    state = state.copy(
                        isGrid = it
                    )
                }
            }
            loadAll()
            loadPinned()
        }
    }

    private fun loadAll() {
        viewModelScope.launch(Dispatchers.IO) {
            val allPlaylist = async {
                db.readPlaylistPreview().collect {
                    state = state.copy(
                        data = state.data.copy(
                            all = state.data.all.copy(
                                playlist = it.groupBy { result -> result.name }.map { entry ->
                                    UiPlaylistPrev(
                                        id = entry.value[0].playlistId,
                                        name = entry.key,
                                        listOfUrl = entry.value.map { url ->
                                            url.coverImage
                                        }.shuffled(Random()).take(4)
                                    )
                                }
                            )
                        )
                    )
                }
            }

            val allAlbum = async {
                db.readAllAlbumPreview().collect {
                    state = state.copy(
                        data = state.data.copy(
                            all = state.data.all.copy(
                                album = it.groupBy { album ->
                                    album.id
                                }.map { entry ->
                                    UiAlbumPrev(
                                        id = entry.key,
                                        name = entry.value[0].name,
                                        coverImage = entry.value[0].coverImage
                                    )
                                }
                            )
                        )
                    )
                }
            }

            val allArtist = async {
                db.readAllArtist().collect {
                    state = state.copy(
                        data = state.data.copy(
                            all = state.data.all.copy(
                                artist = it
                            )
                        )
                    )
                }
            }

            val allFavourite = async {
                state = state.copy(
                    data = state.data.copy(
                        all = state.data.all.copy(
                            isFavourite = db.readFavouritePrev() > 0
                        )
                    )
                )
            }

            allPlaylist.await()
            allArtist.await()
            allAlbum.await()
            allFavourite.await()
        }.let {
            viewModelScope.launch(Dispatchers.IO) {
                delay(800)
                state = state.copy(
                    isLoading = false,
                    isInternetError = network.value != NetworkObserver.STATUS.AVAILABLE
                )
            }
        }
    }

    private fun loadPinned() {
        viewModelScope.launch(Dispatchers.IO) {
            val playlist = async {
                db.readPinnedPlaylist().collect {
                    state = state.copy(
                        data = state.data.copy(
                            pinned = state.data.pinned.copy(
                                playlist = it.groupBy { result -> result.name }
                                    .map { map ->
                                        UiPlaylistPrev(
                                            id = map.value[0].playlistId,
                                            name = map.key,
                                            listOfUrl = map.value.map { playlist ->
                                                playlist.coverImage
                                            }.shuffled(Random()).take(4)
                                        )
                                    }
                            )
                        )
                    )
                }
            }

            val album = async {
                db.readPinnedAlbum().collect {
                    state = state.copy(
                        data = state.data.copy(
                            pinned = state.data.pinned.copy(
                                album = it.groupBy { album ->
                                    album.name
                                }.map { entry ->
                                    UiAlbumPrev(
                                        id = entry.value[0].id,
                                        name = entry.key,
                                        coverImage = entry.value[0].coverImage
                                    )
                                }
                            )
                        )
                    )
                }
            }

            val isFavourite = async {
                ds.readFavouritePinnedState().collect {
                    state = state.copy(
                        data = state.data.copy(
                            pinned = state.data.pinned.copy(
                                isFavourite = it
                            )
                        )
                    )
                }
            }

            val artist = async {
                db.readPinnedArtist().collect {
                    state = state.copy(
                        data = state.data.copy(
                            pinned = state.data.pinned.copy(
                                artist = it
                            )
                        )
                    )
                }
            }

            playlist.await()
            album.await()
            isFavourite.await()
            artist.await()
        }
    }


    fun onEvent(event: LibraryUiEvent) {
        when (event) {
            is LibraryUiEvent.EmitToast -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiEvent.send(UiEvent.ShowToast(event.message))
                }
            }

            LibraryUiEvent.SomethingWentWrong -> {
                onEvent(LibraryUiEvent.EmitToast("Opp's Something went wrong."))
            }

            LibraryUiEvent.HideBottomSheet -> {
                if (state.isBottomSheetOpen) {
                    state = state.copy(
                        isBottomSheetOpen = false
                    )
                }
            }

            is LibraryUiEvent.FilterChipClick -> {
                when (event) {
                    LibraryUiEvent.FilterChipClick.PlaylistType -> {
                        state = state.copy(
                            filterChip = state.filterChip.copy(
                                isPlaylist = !state.filterChip.isPlaylist
                            )
                        )
                    }

                    LibraryUiEvent.FilterChipClick.ArtistType -> {
                        state = state.copy(
                            filterChip = state.filterChip.copy(
                                isArtist = !state.filterChip.isArtist
                            )
                        )
                    }

                    LibraryUiEvent.FilterChipClick.AlbumType -> {
                        state = state.copy(
                            filterChip = state.filterChip.copy(
                                isAlbum = !state.filterChip.isAlbum
                            )
                        )
                    }
                }
            }

            is LibraryUiEvent.ItemClick -> {
                when (event) {
                    LibraryUiEvent.ItemClick.SortTypeClick -> {
                        viewModelScope.launch(Dispatchers.IO) {
                            ds.storeLibraryDataSortType(
                                sortType = !state.isGrid
                            )
                        }
                    }

                    LibraryUiEvent.ItemClick.AddAlbumClick -> {
                        viewModelScope.launch(Dispatchers.IO) {
                            _uiEvent.send(UiEvent.Navigate(Screens.AddAlbum.route))
                        }
                    }

                    LibraryUiEvent.ItemClick.AddArtistClick -> {
                        viewModelScope.launch(Dispatchers.IO) {
                            _uiEvent.send(UiEvent.Navigate(Screens.AddArtist.route))
                        }
                    }

                    LibraryUiEvent.ItemClick.CreatePlaylistClick -> {
                        viewModelScope.launch(Dispatchers.IO) {

                        }
                    }


                    LibraryUiEvent.ItemClick.FavouriteLongClick -> {
                        viewModelScope.launch(Dispatchers.IO) {
                            async {
                                state = state.copy(
                                    pinnedData = state.pinnedData.copy(
                                        name = PinnedDataType.FAVOURITE.title,
                                        type = PinnedDataType.FAVOURITE,
                                        isPinned = ds.readFavouritePinnedState().first()
                                    )
                                )
                            }.await()

                            state = state.copy(
                                isBottomSheetOpen = true
                            )
                        }
                    }

                    LibraryUiEvent.ItemClick.FavouriteClick -> {
                        viewModelScope.launch(Dispatchers.IO) {
                            _uiEvent.send(
                                UiEvent.NavigateWithData(
                                    itemsType = ItemsType.FAVOURITE,
                                    route = Screens.SongView.route
                                )
                            )
                        }
                    }

                    is LibraryUiEvent.ItemClick.PlaylistLongClick -> {
                        viewModelScope.launch(Dispatchers.IO) {
                            async {
                                state = state.copy(
                                    pinnedData = state.pinnedData.copy(
                                        name = event.name,
                                        type = PinnedDataType.PLAYLIST,
                                        isPinned = db.checkIfPlaylistIdPinned(name = event.name)
                                    )
                                )
                            }.await()

                            state = state.copy(
                                isBottomSheetOpen = true
                            )
                        }
                    }

                    is LibraryUiEvent.ItemClick.PlaylistClick -> {
                        viewModelScope.launch(Dispatchers.IO) {
                            _uiEvent.send(
                                UiEvent.NavigateWithData(
                                    route = Screens.SongView.route,
                                    itemsType = ItemsType.PLAYLIST,
                                    id = event.id
                                )
                            )
                        }
                    }

                    is LibraryUiEvent.ItemClick.AlbumLongClick -> {
                        viewModelScope.launch(Dispatchers.IO) {
                            async {
                                state = state.copy(
                                    pinnedData = state.pinnedData.copy(
                                        name = event.name,
                                        type = PinnedDataType.ALBUM,
                                        isPinned = db.checkIfAlbumPinned(name = event.name)
                                    )
                                )
                            }.await()

                            state = state.copy(
                                isBottomSheetOpen = true
                            )
                        }
                    }

                    is LibraryUiEvent.ItemClick.AlbumClick -> {
                        viewModelScope.launch(Dispatchers.IO) {
                            _uiEvent.send(
                                UiEvent.NavigateWithData(
                                    route = Screens.SongView.route,
                                    itemsType = ItemsType.ALBUM,
                                    name = event.name
                                )
                            )
                        }
                    }


                    is LibraryUiEvent.ItemClick.ArtistLongClick -> {
                        viewModelScope.launch(Dispatchers.IO) {
                            async {
                                state = state.copy(
                                    pinnedData = state.pinnedData.copy(
                                        name = event.name,
                                        type = PinnedDataType.ARTIST,
                                        isPinned = db.checkIfArtistPinned(name = event.name)
                                    )
                                )
                            }.await()

                            state = state.copy(
                                isBottomSheetOpen = true
                            )
                        }
                    }

                    is LibraryUiEvent.ItemClick.ArtistClick -> {
                        viewModelScope.launch(Dispatchers.IO) {
                            _uiEvent.send(
                                UiEvent.NavigateWithData(
                                    route = Screens.SongView.route,
                                    itemsType = ItemsType.ARTIST,
                                    id = event.id,
                                    name = event.name
                                )
                            )
                        }
                    }

                    is LibraryUiEvent.BottomSheetItemClick -> {
                        when (event) {
                            is LibraryUiEvent.BottomSheetItemClick.AddClick -> {
                                viewModelScope.launch(Dispatchers.IO) {
                                    state = state.copy(
                                        isBottomSheetOpen = false
                                    )

                                    val id = db.addToPinnedTable(
                                        type = event.type,
                                        name = event.name,
                                        ds = ds
                                    )



                                    if (event.type != PinnedDataType.NON && id != -1L) {
                                        val response = api.handlePin(
                                            req = PinnedReq(
                                                type = when (event.type) {
                                                    PinnedDataType.PLAYLIST -> IdType.PLAYLIST
                                                    PinnedDataType.ALBUM -> IdType.ALBUM
                                                    PinnedDataType.ARTIST -> IdType.ARTIST
                                                    else -> return@launch
                                                },
                                                id = id,
                                                operation = PinnedOperation.ADD
                                            )
                                        )

                                        if (!response) {
                                            db.addToInternalPinnedTable(
                                                data = InternalPinnedTable(
                                                    pinnedId = id,
                                                    type = when (event.type) {
                                                        PinnedDataType.PLAYLIST -> IdType.PLAYLIST
                                                        PinnedDataType.ALBUM -> IdType.ALBUM
                                                        PinnedDataType.ARTIST -> IdType.ARTIST
                                                        else -> return@launch
                                                    },
                                                    operation = PinnedOperation.ADD
                                                )
                                            )
                                        }
                                    }
                                }
                            }

                            LibraryUiEvent.BottomSheetItemClick.RemoveClick -> {
                                state = state.copy(
                                    isBottomSheetOpen = false
                                )

                                viewModelScope.launch(Dispatchers.IO) {
                                    if (state.pinnedData.type == PinnedDataType.NON ||
                                        state.pinnedData.name.isEmpty()
                                    ) {
                                        onEvent(LibraryUiEvent.SomethingWentWrong)
                                        return@launch
                                    }

                                    val id = db.removeFromPinnedTable(
                                        type = state.pinnedData.type,
                                        name = state.pinnedData.name,
                                        ds = ds
                                    )

                                    if (id == -1L) {
                                        onEvent(LibraryUiEvent.SomethingWentWrong)
                                        return@launch
                                    }

                                    val response = api.handlePin(
                                        req = PinnedReq(
                                            type = when (state.pinnedData.type) {
                                                PinnedDataType.PLAYLIST -> IdType.PLAYLIST
                                                PinnedDataType.ALBUM -> IdType.ALBUM
                                                PinnedDataType.ARTIST -> IdType.ARTIST
                                                else -> return@launch
                                            },
                                            id = id,
                                            operation = PinnedOperation.REMOVE
                                        )
                                    )

                                    db.removeFromPinnedTable(
                                        data = InternalPinnedTable(
                                            pinnedId = id,
                                            type = when (state.pinnedData.type) {
                                                PinnedDataType.PLAYLIST -> IdType.PLAYLIST
                                                PinnedDataType.ALBUM -> IdType.ALBUM
                                                PinnedDataType.ARTIST -> IdType.ARTIST
                                                else -> return@launch
                                            },
                                            operation = PinnedOperation.REMOVE
                                        ),
                                        response = response
                                    )
                                }
                            }

                            LibraryUiEvent.BottomSheetItemClick.DeleteClick -> {
                                viewModelScope.launch(Dispatchers.IO) {
                                    if (state.pinnedData.name.isEmpty()) {
                                        onEvent(LibraryUiEvent.SomethingWentWrong)

                                        state = state.copy(
                                            isBottomSheetOpen = false
                                        )

                                        return@launch
                                    }

                                    if (!state.isDialogOpen) state =
                                        state.copy(isDialogOpen = true) // open dialog
                                }
                            }
                        }
                    }

                    is LibraryUiEvent.DeleteDialogClick -> {
                        when (event) {
                            LibraryUiEvent.DeleteDialogClick.DeleteYes -> {
                                viewModelScope.launch(Dispatchers.IO) {
                                    state = state.copy(
                                        isDialogOpen = false,
                                        isBottomSheetOpen = false
                                    )

                                    if (state.pinnedData.name.isEmpty()) {
                                        onEvent(LibraryUiEvent.SomethingWentWrong)

                                        return@launch
                                    }

                                    val id = db.deletePlaylistArtistAlbumFavouriteEntry(
                                        type = state.pinnedData.type,
                                        name = state.pinnedData.name,
                                        ds = ds
                                    )

                                    val response = api.handleItem(
                                        req = ItemReq(
                                            id = id,
                                            type = when (state.pinnedData.type) {
                                                PinnedDataType.PLAYLIST -> IdType.PLAYLIST
                                                PinnedDataType.ALBUM -> IdType.ALBUM
                                                PinnedDataType.ARTIST -> IdType.ARTIST
                                                else -> return@launch
                                            },
                                            operation = ItemOperation.DELETE
                                        )
                                    )

                                    if (!response) db.addToInternalItemTable(
                                        data = InternalItemTable(
                                            itemId = id,
                                            type = when (state.pinnedData.type) {
                                                PinnedDataType.PLAYLIST -> IdType.PLAYLIST
                                                PinnedDataType.ALBUM -> IdType.ALBUM
                                                PinnedDataType.ARTIST -> IdType.ARTIST
                                                else -> return@launch
                                            },
                                            operation = ItemOperation.DELETE
                                        )
                                    )
                                }
                            }

                            LibraryUiEvent.DeleteDialogClick.DeleteNo -> {
                                state = state.copy(
                                    isDialogOpen = false,
                                    isBottomSheetOpen = false
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}