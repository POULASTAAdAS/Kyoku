package com.poulastaa.explore.presentation.search.all_from_artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.UiText
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.toUiPrevArtist
import com.poulastaa.core.presentation.designsystem.ui.ERROR_LOTTIE_ID
import com.poulastaa.explore.domain.model.ExploreAllowedNavigationScreen
import com.poulastaa.explore.domain.repository.AllFromArtistRepository
import com.poulastaa.explore.presentation.model.ExploreUiItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
internal class AllFromArtistViewModel @Inject constructor(
    private val repo: AllFromArtistRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(AllFromArtistUiState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
        initialValue = AllFromArtistUiState()
    )

    private val _song: MutableStateFlow<PagingData<ExploreUiItem>> =
        MutableStateFlow(PagingData.empty())
    var song = _song.asStateFlow()
        private set

    var _album: MutableStateFlow<PagingData<ExploreUiItem>> =
        MutableStateFlow(PagingData.empty())
    var album = _album.asStateFlow()
        private set

    private val _uiEvent = Channel<AllFromArtistUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var getSongJob: Job? = null
    private var getAlbumJob: Job? = null

    fun init(artistId: ArtistId) {
        getArtist(artistId)

        getSongJob?.cancel()
        getAlbumJob?.cancel()
        getSongJob = getPagingSong(artistId)
        getAlbumJob = getPagingAlbum(artistId)
    }

    fun onAction(action: AllFromArtistUiAction) {
        when (action) {
            is AllFromArtistUiAction.OnAlbumClick -> {
                viewModelScope.launch {
                    _uiEvent.send(
                        AllFromArtistUiEvent.Navigate(
                            ExploreAllowedNavigationScreen.Album(action.albumId)
                        )
                    )
                }
            }

            is AllFromArtistUiAction.OnFilterChange -> {
                if (action.filterType == state.value.filterType) return

                _state.update {
                    it.copy(
                        filterType = action.filterType
                    )
                }
            }

            is AllFromArtistUiAction.OnSearchQueryChange -> {
                _state.update {
                    it.copy(
                        query = it.query.copy(
                            value = action.query
                        )
                    )
                }

                getSongJob?.cancel()
                getAlbumJob?.cancel()
                getSongJob = getPagingSong(_state.value.artist.id)
                getAlbumJob = getPagingAlbum(_state.value.artist.id)
            }

            AllFromArtistUiAction.OnSearchQueryClear -> {
                _state.update {
                    it.copy(
                        query = it.query.copy(
                            value = ""
                        )
                    )
                }

                getSongJob?.cancel()
                getAlbumJob?.cancel()
                getSongJob = getPagingSong(_state.value.artist.id)
                getAlbumJob = getPagingAlbum(_state.value.artist.id)
            }

            is AllFromArtistUiAction.OnSongClick -> {
                // todo add to playing queue
            }

            AllFromArtistUiAction.OnToggleSearch -> {
                _state.update {
                    it.copy(
                        isSearchOpen = it.isSearchOpen.not()
                    )
                }
            }
        }
    }

    private fun getArtist(artistId: ArtistId) {
        viewModelScope.launch {
            val result = repo.getArtist(artistId)

            when (result) {
                is Result.Error -> when (result.error) {
                    DataError.Network.NO_INTERNET -> _uiEvent.send(
                        AllFromArtistUiEvent.EmitToast(
                            UiText.StringResource(R.string.error_no_internet)
                        )
                    ).also {
                        _state.update {
                            it.copy(
                                loadingType = LoadingType.Error(
                                    type = LoadingType.ERROR_TYPE.NO_INTERNET,
                                    lottieId = ERROR_LOTTIE_ID
                                )
                            )
                        }
                    }

                    else -> _uiEvent.send(
                        AllFromArtistUiEvent.EmitToast(
                            UiText.StringResource(R.string.error_something_went_wrong)
                        )
                    ).also {
                        _state.update {
                            it.copy(
                                loadingType = LoadingType.Error(
                                    type = LoadingType.ERROR_TYPE.UNKNOWN,
                                    lottieId = ERROR_LOTTIE_ID
                                )
                            )
                        }
                    }
                }

                is Result.Success -> _state.update {
                    it.copy(
                        artist = result.data.toUiPrevArtist()
                    )
                }
            }
        }
    }

    private fun getPagingAlbum(artistId: ArtistId) = viewModelScope.launch {
        _album.update { PagingData.empty() }

        repo.getAlbums(artistId, _state.value.query.value.trim())
            .cachedIn(viewModelScope)
            .collectLatest { result ->
                _album.update {
                    result.map { item -> item.toAllFromArtistUiItem() }
                }
            }
    }

    private fun getPagingSong(artistId: ArtistId) = viewModelScope.launch {
        _song.update { PagingData.empty() }

        repo.getSongs(artistId, _state.value.query.value.trim())
            .cachedIn(viewModelScope)
            .collectLatest { result ->
                _song.update {
                    result.map { item -> item.toAllFromArtistUiItem() }
                }
            }
    }
}