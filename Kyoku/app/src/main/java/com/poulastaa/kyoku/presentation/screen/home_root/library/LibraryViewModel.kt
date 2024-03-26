package com.poulastaa.kyoku.presentation.screen.home_root.library

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.kyoku.connectivity.NetworkObserver
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent
import com.poulastaa.kyoku.data.model.screens.common.UiPlaylistPrev
import com.poulastaa.kyoku.data.model.screens.library.LibraryUiEvent
import com.poulastaa.kyoku.data.model.screens.library.LibraryUiState
import com.poulastaa.kyoku.data.repository.DatabaseRepositoryImpl
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import com.poulastaa.kyoku.domain.repository.ServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
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

    fun loadData(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            delay(800)
            state = state.copy(
                isLoading = false
            )
        }

        if (!state.data.all.isFavourite &&
            state.data.all.playlist.isEmpty() &&
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

            // read All Data
            viewModelScope.launch(Dispatchers.IO) {
                val allPlaylist = async {
                    db.readPlaylistPreview().collect {
                        state = state.copy(
                            data = state.data.copy(
                                all = state.data.all.copy(
                                    playlist = it.groupBy { result -> result.name }.map { entry ->
                                        UiPlaylistPrev(
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
                allFavourite.await()
            }

            // read Pinned Data
            viewModelScope.launch(Dispatchers.IO) {

            }
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

            LibraryUiEvent.SortTypeClick -> {
                viewModelScope.launch(Dispatchers.IO) {
                    ds.storeLibraryDataSortType(
                        sortType = !state.isGrid
                    )
                }
            }
        }
    }
}