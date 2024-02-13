package com.poulastaa.kyoku.presentation.screen.setup.birth_date

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.kyoku.connectivity.NetworkObserver
import com.poulastaa.kyoku.data.model.auth.UiEvent
import com.poulastaa.kyoku.data.model.setup.set_birth_date.SetBirthDateUiEvent
import com.poulastaa.kyoku.data.model.setup.set_birth_date.SetBirthDateUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetBirthDateViewModel @Inject constructor(
    private val connectivity: NetworkObserver,
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

    var state by mutableStateOf(SetBirthDateUiState())
        private set

    fun onEvent(event: SetBirthDateUiEvent) {
        when (event) {
            is SetBirthDateUiEvent.OnDateSelected -> {
                state = state.copy(
                    bDate = event.date
                )
            }

            SetBirthDateUiEvent.OnDateSelectorClicked -> {
                state = state.copy(
                    isDateSelected = true
                )
            }

            SetBirthDateUiEvent.OnContinueClick -> {
                if (state.bDate.isEmpty())
                    onEvent(SetBirthDateUiEvent.EmitToast("Please Select Your Birth Date"))
                // todo add more check
            }

            is SetBirthDateUiEvent.EmitToast -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiEvent.send(UiEvent.ShowToast(event.message))
                }
            }
        }
    }
}