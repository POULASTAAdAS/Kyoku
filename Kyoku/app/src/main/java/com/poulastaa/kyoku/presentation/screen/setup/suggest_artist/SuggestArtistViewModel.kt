package com.poulastaa.kyoku.presentation.screen.setup.suggest_artist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.kyoku.connectivity.NetworkObserver
import com.poulastaa.kyoku.data.model.SignInStatus
import com.poulastaa.kyoku.data.model.api.auth.AuthType
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_artist.ArtistResponseStatus
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_artist.SuggestArtistReq
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent
import com.poulastaa.kyoku.data.model.screens.setup.suggest_artist.SuggestArtistUiEvent
import com.poulastaa.kyoku.data.model.screens.setup.suggest_artist.SuggestArtistUiState
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import com.poulastaa.kyoku.domain.repository.ServiceRepository
import com.poulastaa.kyoku.utils.storeSignInState
import com.poulastaa.kyoku.utils.toAlreadySendArtistList
import com.poulastaa.kyoku.utils.toArtistNameList
import com.poulastaa.kyoku.utils.toStoreArtistReq
import com.poulastaa.kyoku.utils.toUiArtistList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SuggestArtistViewModel @Inject constructor(
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

    private var suggestArtistJob: Job? = null
    private var selectedArtist: Int = 0

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var state by mutableStateOf(SuggestArtistUiState())
        private set


    private fun readAuthType() {
        viewModelScope.launch {
            state = when (ds.readAuthType().first()) {
                AuthType.JWT_AUTH.name -> {
                    state.copy(
                        isCookie = false
                    )
                }

                else -> {
                    state.copy(
                        isCookie = true
                    )
                }
            }
        }
    }

    private fun readAuthHeader() {
        viewModelScope.launch {
            ds.readTokenOrCookie().collect {
                state = state.copy(
                    hearValue = it
                )
            }
        }
    }

    init {
        readAuthType()
        readAuthHeader()
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            delay(300) // making sure state.isInternetAvailable in loaded
            if (state.isFirstApiCall && state.isInternetAvailable) {
                val response = api.suggestArtist(
                    req = SuggestArtistReq(
                        isSelected = false,
                        alreadySendArtistList = emptyList()
                    )
                )

                state = when (response.status) {
                    ArtistResponseStatus.SUCCESS -> {
                        state.copy(
                            data = response.toUiArtistList(),
                            isFirstApiCall = false
                        )
                    }

                    ArtistResponseStatus.FAILURE -> {
                        onEvent(SuggestArtistUiEvent.SomethingWentWrong)
                        state.copy(
                            isFirstApiCall = false
                        )
                    }
                }
            }

            if (!state.isInternetAvailable) onEvent(SuggestArtistUiEvent.EmitToast("Please check Your Internet Connection"))
        }
    }

    fun onEvent(event: SuggestArtistUiEvent) {
        when (event) {
            is SuggestArtistUiEvent.OnArtistClick -> {
                state = state.copy(
                    data = state.data.map {
                        if (it.name == event.name) {
                            val isSelected = !it.isSelected

                            updateSelectedArtist(isSelected)

                            if (isSelected && state.isAnyArtistLeft) {
                                if (state.isInternetAvailable) {
                                    suggestArtistJob?.cancel()
                                    suggestArtistJob =
                                        requestExtraArtist(state.data.indexOf(it) + 1)
                                } else {
                                    onEvent(SuggestArtistUiEvent.EmitToast("Please check Your Internet Connection"))
                                }
                            }

                            it.copy(isSelected = isSelected)
                        } else it
                    }
                )
            }

            SuggestArtistUiEvent.OnContinueClick -> {
                if (selectedArtist < 3) {
                    onEvent(SuggestArtistUiEvent.EmitToast("Please select at-list 4 Artist"))
                    return
                }

                if (state.isInternetAvailable) {
                    if (!state.isSendingDataToApi) {
                        state = state.copy(
                            isSendingDataToApi = true
                        )

                        storeArtist(state.data.toArtistNameList())
                    }
                }
            }

            is SuggestArtistUiEvent.EmitToast -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiEvent.send(UiEvent.ShowToast(event.message))
                }
            }

            SuggestArtistUiEvent.SomethingWentWrong -> {
                onEvent(SuggestArtistUiEvent.EmitToast("Opp's something went wrong"))
            }
        }
    }

    private fun storeArtist(artistNameList: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = api.storeArtist(artistNameList.toStoreArtistReq())

            when (response.status) {
                ArtistResponseStatus.SUCCESS -> {
                    storeSignInState(SignInStatus.HOME, ds)
                }

                ArtistResponseStatus.FAILURE -> {
                    onEvent(SuggestArtistUiEvent.EmitToast("Opp's something went wrong"))
                }
            }

            state = state.copy(
                isSendingDataToApi = false
            )
        }
    }

    private fun updateSelectedArtist(isSelected: Boolean) {
        if (isSelected) selectedArtist += 1
        else if (selectedArtist > 0) selectedArtist -= 1
    }

    private fun requestExtraArtist(index: Int): Job = viewModelScope.launch(Dispatchers.IO) {
        val response = api.suggestArtist(
            req = SuggestArtistReq(
                isSelected = true,
                alreadySendArtistList = state.data.toAlreadySendArtistList()
            )
        )

        when (response.status) {
            ArtistResponseStatus.SUCCESS -> {
                val newList = response.toUiArtistList()

                state = if (newList.isNotEmpty()) {
                    val data = state.data.toMutableList()

                    data.addAll(index, newList)

                    state.copy(
                        data = data
                    )
                } else state.copy(
                    isAnyArtistLeft = false
                )

            }

            ArtistResponseStatus.FAILURE -> Unit
        }
    }
}