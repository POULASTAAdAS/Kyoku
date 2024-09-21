package com.poulastaa.play.presentation.add_new_album

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.repository.new_album.NewAlbumRepository
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.ui.SnackBarType
import com.poulastaa.core.presentation.ui.SnackBarUiState
import com.poulastaa.core.presentation.ui.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNewAlbumViewModel @Inject constructor(
    private val ds: DataStoreRepository,
    private val repo: NewAlbumRepository,
) : ViewModel() {
    var state by mutableStateOf(AddAlbumUiState())
        private set

    private val _uiEvent = Channel<AddNewAlbumUiAction>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _album: MutableStateFlow<PagingData<AddAlbumUiAlbum>> =
        MutableStateFlow(PagingData.empty())
    var album = _album.asStateFlow()
        private set

    private var loadDataJob: Job? = null
    private var showSnackBarJob: Job? = null

    private var saveAlbumIdList = mutableListOf<Long>()

    init {
        readHeader()
        loadDataJob?.cancel()
        loadDataJob = loadData()
    }

    fun onEvent(event: AddAlbumUiEvent) {
        if (state.isMakingApiCall) return

        when (event) {
            is AddAlbumUiEvent.OnAlbumClick -> {
                viewModelScope.launch {
                    _uiEvent.send(
                        AddNewAlbumUiAction.Navigate(
                            AddNewAlbumOtherScreen.ViewAlbum(
                                event.albumId
                            )
                        )
                    )
                }
            }

            AddAlbumUiEvent.OnSearchToggle -> {
                state = state.copy(
                    isSearchEnabled = state.isSearchEnabled.not()
                )

                state = state.copy(searchQuery = "")

                loadDataJob?.cancel()
                loadDataJob = loadData()
            }

            is AddAlbumUiEvent.OnSearchQueryChange -> {
                if (state.searchQuery == event.query.trim()) return

                state = state.copy(
                    searchQuery = event.query.trim()
                )

                loadDataJob?.cancel()
                loadDataJob = loadData()
            }

            is AddAlbumUiEvent.OnFilterTypeChange -> {
                if (state.type == event.type) return

                state = state.copy(
                    type = event.type
                )

                loadDataJob?.cancel()
                loadDataJob = loadData()
            }

            is AddAlbumUiEvent.OnMassSelectToggle -> {
                if (state.isMassSelectEnabled) saveAlbumIdList.clear()

                _album.value = _album.value.map {
                    it.copy(isSelected = false)
                }

                state = state.copy(
                    isMassSelectEnabled = state.isMassSelectEnabled.not()
                )
            }

            is AddAlbumUiEvent.OnCheckChange -> {
                _album.value = _album.value.map {
                    if (it.id == event.id) it.copy(isSelected = event.status)
                    else it
                }

                if (event.status) saveAlbumIdList.add(event.id)
                else saveAlbumIdList.remove(event.id)
            }

            is AddAlbumUiEvent.OnSaveClick -> {
                state = state.copy(
                    isMakingApiCall = true
                )

                saveAlbums(saveAlbumIdList.toList())
            }

            is AddAlbumUiEvent.ThreeDotEvent -> {
                when (event) {
                    is AddAlbumUiEvent.ThreeDotEvent.OnClick -> {
                        _album.value = _album.value.map {
                            if (it.id == event.id) {
                                it.copy(isExtended = true)
                            } else it
                        }
                    }

                    is AddAlbumUiEvent.ThreeDotEvent.OnCloseClick -> {
                        _album.value = _album.value.map {
                            if (it.id == event.id) it.copy(isExtended = false)
                            else it
                        }
                    }

                    is AddAlbumUiEvent.ThreeDotEvent.OnThreeDotItemClick -> {
                        _album.value = _album.value.map {
                            if (it.id == event.id) it.copy(isExtended = false)
                            else it
                        }

                        when (event.operation) {
                            AddAlbumOperation.PLAY -> {

                            }

                            AddAlbumOperation.SAVE_ALBUM -> {
                                saveAlbums(listOf(event.id))

                                showSnackBarJob?.cancel()
                                showSnackBarJob = showSnackBar(
                                    type = SnackBarType.SUCCESS,
                                    message = UiText.StringResource(R.string.album_added_to_library)
                                )
                            }
                        }
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

    private fun loadData() = viewModelScope.launch {
        _album.value = PagingData.empty()

        repo.getPagingAlbum(
            query = state.searchQuery.trim(),
            type = state.type
        ).cachedIn(viewModelScope).collectLatest {
            _album.value = it.map { data ->
                data.toUiAlbum()
            }
        }
    }

    private fun showSnackBar(type: SnackBarType, message: UiText) = viewModelScope.launch {
        state = state.copy(
            toast = SnackBarUiState(
                type = type,
                isVisible = true,
                message = message
            )
        )

        delay(4000)

        state = state.copy(
            toast = SnackBarUiState()
        )
    }

    private fun saveAlbums(idList: List<Long>) {
        if (idList.isEmpty()) {
            state = state.copy(
                isMakingApiCall = false
            )

            return
        }

        viewModelScope.launch {
            when (val result = repo.saveAlbums(idList)) {
                is Result.Error -> {
                    when (result.error) {
                        DataError.Network.NO_INTERNET -> {
                            showSnackBarJob?.cancel()
                            showSnackBarJob = showSnackBar(
                                type = SnackBarType.ERROR,
                                message = UiText.StringResource(R.string.error_no_internet)
                            )
                        }

                        else -> {
                            showSnackBarJob?.cancel()
                            showSnackBarJob = showSnackBar(
                                type = SnackBarType.ERROR,
                                message = UiText.StringResource(R.string.error_something_went_wrong)
                            )
                        }
                    }
                }

                is Result.Success -> {
                    showSnackBarJob?.cancel()
                    showSnackBarJob = showSnackBar(
                        type = SnackBarType.SUCCESS,
                        message = UiText.StringResource(R.string.album_added_to_library)
                    )
                }
            }

            state = state.copy(
                isMakingApiCall = false,
                isMassSelectEnabled = false
            )

            _album.value.map {
                it.copy(isSelected = false)
            }
        }
    }
}