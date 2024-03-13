package com.poulastaa.kyoku.presentation.screen.setup.suggest_genre

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.kyoku.connectivity.NetworkObserver
import com.poulastaa.kyoku.data.model.SignInStatus
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_genre.GenreResponseStatus
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_genre.SuggestGenreReq
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent
import com.poulastaa.kyoku.data.model.screens.setup.suggest_genre.SuggestGenreUiEvent
import com.poulastaa.kyoku.data.model.screens.setup.suggest_genre.SuggestGenreUiState
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import com.poulastaa.kyoku.domain.repository.ServiceRepository
import com.poulastaa.kyoku.utils.storeSignInState
import com.poulastaa.kyoku.utils.toAlreadySendGenreList
import com.poulastaa.kyoku.utils.toGenreNameList
import com.poulastaa.kyoku.utils.toStoreGenreReq
import com.poulastaa.kyoku.utils.toUiGenreList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SuggestGenreViewModel @Inject constructor(
    private val connectivity: NetworkObserver,
    private val api: ServiceRepository,
    private val ds: DataStoreOperation
) : ViewModel() {
    private val network = mutableStateOf(NetworkObserver.STATUS.UNAVAILABLE)

    init {
        viewModelScope.launch {
            connectivity.observe().collect {
                network.value = it
                state = state.copy(
                    isInternetAvailable = checkInternetConnection()
                )
            }
        }
    }


    private fun checkInternetConnection(): Boolean {
        return network.value == NetworkObserver.STATUS.AVAILABLE
    }

    private var suggestGenreJob: Job? = null
    private var selectedGenre: Int = 0

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var state by mutableStateOf(SuggestGenreUiState())
        private set

    init {
        viewModelScope.launch(Dispatchers.IO) {
            delay(500)
            if (state.isFirstApiCall && state.isInternetAvailable) {
                val response = api.suggestGenre(
                    req = SuggestGenreReq(
                        isSelectReq = false,
                        alreadySendGenreList = emptyList()
                    )
                )

                state = when (response.status) {
                    GenreResponseStatus.SUCCESS -> {
                        state.copy(
                            data = response.toUiGenreList(),
                            isFirstApiCall = false
                        )
                    }

                    GenreResponseStatus.FAILURE -> {
                        onEvent(SuggestGenreUiEvent.SomethingWentWrong)
                        state.copy(
                            isFirstApiCall = false
                        )
                    }
                }
            }

            if (!state.isInternetAvailable) onEvent(SuggestGenreUiEvent.EmitToast("Please check Your Internet Connection"))
        }
    }

    fun onEvent(event: SuggestGenreUiEvent) {
        when (event) {
            is SuggestGenreUiEvent.OnGenreClick -> {
                state = state.copy(
                    data = state.data.map {
                        if (it.name == event.name) {
                            val isSelected = !it.isSelected // inverse before operating

                            updateSelectedGenre(isSelected)

                            if (isSelected && state.isAnyGenreLeft) {
                                if (state.isInternetAvailable) {
                                    suggestGenreJob?.cancel()
                                    suggestGenreJob = requestExtraGenre(state.data.indexOf(it) + 1)
                                } else {
                                    onEvent(SuggestGenreUiEvent.EmitToast("Please check Your Internet Connection"))
                                }
                            }

                            it.copy(isSelected = isSelected)
                        } else it
                    }
                )
            }

            SuggestGenreUiEvent.OnContinueClick -> {
                if (selectedGenre < 3) {
                    onEvent(SuggestGenreUiEvent.EmitToast("Please select at-list 4 Genre"))
                    return
                }

                if (state.isInternetAvailable) {
                    if (!state.isSendingDataToApi) {
                        state = state.copy(
                            isSendingDataToApi = true
                        )

                        storeGenre(state.data.toGenreNameList())
                    }
                } else onEvent(SuggestGenreUiEvent.EmitToast("Please check Your Internet Connection"))
            }

            is SuggestGenreUiEvent.EmitToast -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiEvent.send(UiEvent.ShowToast(event.message))
                }
            }

            SuggestGenreUiEvent.SomethingWentWrong -> {
                onEvent(SuggestGenreUiEvent.EmitToast("Opp's something went wrong"))
            }
        }
    }

    private fun storeGenre(genreNameList: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = api.storeGenre(genreNameList.toStoreGenreReq())

            when (response.status) {
                GenreResponseStatus.SUCCESS -> {
                    storeSignInState(SignInStatus.ARTIST_SET, ds)
                }

                GenreResponseStatus.FAILURE -> {
                    onEvent(SuggestGenreUiEvent.EmitToast("Opp's something went wrong"))
                }
            }

            state = state.copy(
                isSendingDataToApi = false
            )
        }
    }

    private fun updateSelectedGenre(isSelected: Boolean) {
        if (isSelected) selectedGenre += 1
        else if (selectedGenre > 0) selectedGenre -= 1
    }

    private fun requestExtraGenre(index: Int): Job = viewModelScope.launch(Dispatchers.IO) {
        val response = api.suggestGenre(
            req = SuggestGenreReq(
                isSelectReq = true,
                alreadySendGenreList = state.data.toAlreadySendGenreList()
            )
        )

        when (response.status) {
            GenreResponseStatus.SUCCESS -> {
                val newList = response.toUiGenreList()

                state = if (newList.isNotEmpty()) {
                    val data = state.data.toMutableList()

                    data.addAll(index, newList)

                    state.copy(
                        data = data
                    )
                } else {
                    state.copy(
                        isAnyGenreLeft = false
                    )
                }
            }

            GenreResponseStatus.FAILURE -> Unit
        }
    }
}