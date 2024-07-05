package com.poulastaa.setup.presentation.set_artist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.ScreenEnum
import com.poulastaa.core.domain.artist.ArtistRepository
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.ui.UiText
import com.poulastaa.setup.presentation.set_artist.mapper.toArtist
import com.poulastaa.setup.presentation.set_artist.mapper.toUiArtist
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistViewModel @Inject constructor(
    private val ds: DataStoreRepository,
    private val repository: ArtistRepository,
) : ViewModel() {
    var state by mutableStateOf(ArtistUiState())
        private set

    private val selectedArtistIdList: ArrayList<Long> = ArrayList()

    private val _uiEvent = Channel<ArtistUiAction>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var getArtistJob: Job? = null

    init {
        viewModelScope.launch {
            getArtistJob?.cancel()
            getArtistJob = getArtist()
        }
    }

    init {
        viewModelScope.launch {
            ds.readTokenOrCookie().collectLatest {
                state = state.copy(
                    header = it
                )
            }
        }
    }

    fun onEvent(event: ArtistUiEvent) {
        when (event) {
            is ArtistUiEvent.OnArtistClick -> {
                var shouldMakeCall = false

                state = state.copy(
                    data = state.data.map {
                        if (it.id == event.id) {
                            if (selectedArtistIdList.contains(it.id)) selectedArtistIdList.remove(it.id)
                            else selectedArtistIdList.add(it.id)

                            shouldMakeCall = !it.isSelected

                            it.copy(isSelected = !it.isSelected)
                        } else it
                    }
                )

                state = if (selectedArtistIdList.size > 4) state.copy(
                    isToastVisible = false,
                    canMakeApiCall = true
                ) else state.copy(
                    isToastVisible = true,
                    canMakeApiCall = false
                )


                if (shouldMakeCall) {
                    getArtistJob?.cancel()
                    getArtistJob = getArtist()
                }
            }

            ArtistUiEvent.OnContinueClick -> {
                if (selectedArtistIdList.size < 4) {
                    state = state.copy(
                        isToastVisible = true,
                        canMakeApiCall = false
                    )

                    return
                }

                state = state.copy(
                    isMakingApiCall = true,
                    isInternetErr = false
                )


                viewModelScope.launch {
                    val artistList = state.data.filter {
                        selectedArtistIdList.contains(it.id)
                    }.map {
                        it.toArtist()
                    }

                    when (val response = repository.insertArtists(artistList)) {
                        is Result.Error -> {
                            when (response.error) {
                                DataError.Network.NO_INTERNET -> {
                                    _uiEvent.send(
                                        ArtistUiAction.EmitToast(
                                            UiText.StringResource(R.string.error_no_internet)
                                        )
                                    )
                                }

                                else -> {
                                    _uiEvent.send(
                                        ArtistUiAction.EmitToast(
                                            UiText.StringResource(R.string.error_something_went_wrong)
                                        )
                                    )
                                }
                            }
                        }

                        is Result.Success -> {
                            ds.storeSignInState(ScreenEnum.HOME)
                            _uiEvent.send(ArtistUiAction.NavigateToHome)
                        }
                    }

                    state = state.copy(
                        isMakingApiCall = false
                    )
                }
            }
        }
    }

    private fun getArtist() = viewModelScope.launch {
        val response = repository.getArtist(
            state.data.map {
                it.id
            }
        )

        when (response) {
            is Result.Error -> {
                when (response.error) {
                    DataError.Network.NO_INTERNET -> {
                        _uiEvent.send(
                            ArtistUiAction.EmitToast(UiText.StringResource(R.string.error_no_internet))
                        )
                    }

                    else -> Unit
                }
            }

            is Result.Success -> {
                val data = response.data.map {
                    it.toUiArtist()
                }

                state = state.copy(
                    data = state.data + data,
                    isInternetErr = false
                )
            }
        }
    }
}