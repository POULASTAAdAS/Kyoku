package com.poulastaa.auth.presentation.email.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.auth.domain.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmailLoginViewModel @Inject constructor(
    private val userDataValidator: Validator,
) : ViewModel() {
    var state by mutableStateOf(EmailLogInUiState())
        private set

    private val _uiEvent = Channel<EmailLoginUiAction>()
    val uiAction = _uiEvent.receiveAsFlow()

    fun onEvent(event: EmailLoginUiEvent) {
        when (event) {
            is EmailLoginUiEvent.OnEmailChange -> {

            }

            is EmailLoginUiEvent.OnPasswordChange -> {

            }

            EmailLoginUiEvent.OnForgotPasswordClick -> {
                viewModelScope.launch {
                    _uiEvent.send(EmailLoginUiAction.OnForgotPassword(email = state.email.data))
                }
            }

            EmailLoginUiEvent.OnEmailSignUpClick -> {
                viewModelScope.launch {
                    _uiEvent.send(EmailLoginUiAction.OnEmailSignUp)
                }
            }

            EmailLoginUiEvent.OnContinueClick -> {

            }

            EmailLoginUiEvent.OnResendMailClick -> {

            }

            EmailLoginUiEvent.OnPasswordVisibilityToggle -> {
                state = state.copy(
                    isPasswordVisible = !state.isPasswordVisible
                )
            }
        }
    }
}