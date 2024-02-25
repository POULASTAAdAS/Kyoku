package com.poulastaa.kyoku.presentation.screen.setup.select_genre

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.kyoku.connectivity.NetworkObserver
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_genre.SuggestGenreReq
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_genre.SuggestGenreResponseStatus
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent
import com.poulastaa.kyoku.data.model.screens.setup.suggest_genre.SuggestGenreUiEvent
import com.poulastaa.kyoku.data.model.screens.setup.suggest_genre.SuggestGenreUiState
import com.poulastaa.kyoku.domain.repository.ServiceRepository
import com.poulastaa.kyoku.utils.toUiGenre
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectGenreViewModel @Inject constructor(
    private val connectivity: NetworkObserver,
    private val api: ServiceRepository
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

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var state by mutableStateOf(SuggestGenreUiState())
        private set

    init {
        if (state.isFirstCall)
            viewModelScope.launch(Dispatchers.IO) {
                val response = api.suggestGenre(
                    req = SuggestGenreReq(
                        isSelectReq = false,
                        alreadySendGenreList = emptyList()
                    )
                )

                when (response.status) {
                    SuggestGenreResponseStatus.SUCCESS -> {
                        Log.d("called", response.genreList.toString())

                        state = state.copy(
                            data = response.toUiGenre()
                        )
                    }

                    SuggestGenreResponseStatus.FAILURE -> {
                        onEvent(SuggestGenreUiEvent.SomethingWentWrong)
                    }
                }

                state = state.copy(
                    isFirstReq = false,
                    isMakingApiCall = false
                )
            }
    }

    fun onEvent(event: SuggestGenreUiEvent) {
        when (event) {
            is SuggestGenreUiEvent.OnGenreClick -> {
                state = state.copy(
                    data = state.data.map {
                        if (it.name == event.name) it.copy(isSelected = !it.isSelected)
                        else it
                    }
                )
            }

            SuggestGenreUiEvent.OnContinueClick -> {
//                if (state.selectedGenre < 3) {
//                    onEvent(SuggestGenreUiEvent.EmitToast("please select at-list 3 genre"))
//                    return
//                }

                Log.d("data", state.data.toString())
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
}