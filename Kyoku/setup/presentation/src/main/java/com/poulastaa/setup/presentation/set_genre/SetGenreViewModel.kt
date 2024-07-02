package com.poulastaa.setup.presentation.set_genre

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.ScreenEnum
import com.poulastaa.core.domain.genre.GenreRepository
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.ui.UiText
import com.poulastaa.setup.presentation.set_genre.mapper.toUiGenre
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetGenreViewModel @Inject constructor(
    private val ds: DataStoreRepository,
    private val repository: GenreRepository,
) : ViewModel() {
    var state by mutableStateOf(GenreUiState())
        private set

    private val selectedIdList: ArrayList<Int> = ArrayList()

    private val _uiEvent = Channel<GenreUiAction>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            getGenreJob?.cancel()
            getGenreJob = getGenre()
        }
    }

    private var getGenreJob: Job? = null

    fun onEvent(event: GenreUiEvent) {
        when (event) {
            is GenreUiEvent.OnGenreClick -> {
                var shouldMakeCall = false

                state = state.copy(
                    data = state.data.map {
                        if (it.id == event.id) {
                            if (selectedIdList.contains(it.id)) selectedIdList.remove(it.id)
                            else selectedIdList.add(it.id)

                            shouldMakeCall = !it.isSelected

                            it.copy(isSelected = !it.isSelected)
                        } else it
                    }
                )

                state = if (selectedIdList.size > 4) state.copy(
                    isToastVisible = false,
                    canMakeApiCall = true
                ) else state.copy(
                    isToastVisible = true,
                    canMakeApiCall = false
                )

                if (shouldMakeCall) {
                    getGenreJob?.cancel()
                    getGenreJob = getGenre()
                }
            }

            GenreUiEvent.OnContinueClick -> {
                if (selectedIdList.size < 4) {
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
                    val response = repository.storeGenre(
                        genre = selectedIdList
                    )

                    when (response) {
                        is Result.Error -> {
                            when (response.error as DataError.Network) {
                                DataError.Network.NO_INTERNET -> {
                                    _uiEvent.send(
                                        GenreUiAction.EmitToast(UiText.StringResource(R.string.error_no_internet))
                                    )

                                    state = state.copy(
                                        isInternetErr = true
                                    )
                                }

                                else -> {
                                    _uiEvent.send(
                                        GenreUiAction.EmitToast(UiText.StringResource(R.string.error_something_went_wrong))
                                    )
                                }
                            }
                        }

                        is Result.Success -> {
                            ds.storeSignInState(ScreenEnum.PIC_ARTIST)
                            _uiEvent.send(GenreUiAction.NavigateToSetArtist)
                        }
                    }

                    state = state.copy(
                        isMakingApiCall = false
                    )
                }
            }
        }
    }

    private fun getGenre() = viewModelScope.launch {
        val sentIdList = state.data.map {
            it.id
        }

        val response = repository.getGenre(
            sendIfList = sentIdList
        )

        when (response) {
            is Result.Error -> {
                when (response.error) {
                    DataError.Network.NO_INTERNET -> {
                        state = state.copy(
                            isInternetErr = true
                        )

                        _uiEvent.send(
                            GenreUiAction.EmitToast(UiText.StringResource(R.string.error_no_internet))
                        )
                    }

                    else -> Unit
                }
            }

            is Result.Success -> {
                val data = response.data.map {
                    it.toUiGenre()
                }

                state = state.copy(
                    data = state.data + data,
                    isInternetErr = false
                )
            }
        }
    }
}