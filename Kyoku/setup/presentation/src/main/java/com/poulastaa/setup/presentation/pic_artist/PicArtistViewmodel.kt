package com.poulastaa.setup.presentation.pic_artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.UiText
import com.poulastaa.setup.domain.repository.set_artist.SetArtistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PicArtistViewmodel @Inject constructor(
    private val repo: SetArtistRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(PicArtistUiState())
    val state = _state
        .onStart {
            artistJob?.cancel()
            artistJob = loadArtist()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = PicArtistUiState()
        )

    private val _uiEvent = Channel<PicArtistUiEvent>()
    val event = _uiEvent.receiveAsFlow()

    private var artistJob: Job? = null
    private val _artist = MutableStateFlow(PagingData.empty<UiArtist>())
    val artist = _artist.asStateFlow()

    fun onAction(action: PicArtistUiAction) {
        when (action) {
            is PicArtistUiAction.OnQueryChange -> {
                val oldQuery = _state.value.artistQuery.value

                _state.update {
                    it.copy(
                        artistQuery = it.artistQuery.copy(
                            value = action.query.ifEmpty { action.query.trim() }
                        )
                    )
                }

                if (oldQuery == action.query.trim()) return

                viewModelScope.launch {
                    artistJob?.cancel()
                    artistJob = loadArtist()
                }
            }

            is PicArtistUiAction.OnArtistToggle -> {
                _artist.value = _artist.value.map {
                    if (it.id == action.artistId) it.copy(isSelected = action.state.not())
                    else it
                }

                val idList = _state.value.data.toMutableList()

                if (idList.contains(action.artistId)) idList.remove(action.artistId)
                else idList.add(action.artistId)
                _state.update {
                    it.copy(
                        data = idList
                    )
                }
            }

            PicArtistUiAction.OnSubmit -> {
                if (!_state.value.isMinLimitReached || _state.value.isMakingApiCall) return

                viewModelScope.launch {
                    when (val result = repo.storeArtist(_state.value.data)) {
                        is Result.Error -> {
                            when (result.error) {
                                DataError.Network.NO_INTERNET -> _uiEvent.send(
                                    PicArtistUiEvent.EmitToast(
                                        UiText.StringResource(
                                            R.string.error_no_internet
                                        )
                                    )
                                )

                                else -> _uiEvent.send(
                                    PicArtistUiEvent.EmitToast(
                                        UiText.StringResource(
                                            R.string.error_something_went_wrong
                                        )
                                    )
                                )
                            }
                        }

                        is Result.Success -> _uiEvent.send(PicArtistUiEvent.OnSuccess)
                    }

                    _state.update {
                        it.copy(
                            isMakingApiCall = false
                        )
                    }
                }
            }
        }
    }

    private fun loadArtist() = viewModelScope.launch {
        repo.suggestArtist(
            query = _state.value.artistQuery.value.trim()
        ).cachedIn(viewModelScope)
            .collectLatest {
                _artist.value = it.map { dto -> dto.toUiArtist(_state.value.data) }
            }
    }
}