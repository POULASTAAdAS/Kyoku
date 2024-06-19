package com.poulastaa.auth.presentation.email.forgot_password

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.auth.domain.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val validator: Validator,
) : ViewModel() {
    var state by mutableStateOf(ForgotPasswordUiState())
        private set

    private val _uiEvent = Channel<ForgotPasswordUiAction>()
    val uiAction = _uiEvent.receiveAsFlow()

    fun onEvent(event: ForgotPasswordUiEvent) {
        when (event) {
            ForgotPasswordUiEvent.OnBackClick -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiEvent.send(ForgotPasswordUiAction.NavigateBack)
                }
            }

            is ForgotPasswordUiEvent.OnEmailChange -> {

            }

            ForgotPasswordUiEvent.OnSendClick -> {

            }
        }
    }
}