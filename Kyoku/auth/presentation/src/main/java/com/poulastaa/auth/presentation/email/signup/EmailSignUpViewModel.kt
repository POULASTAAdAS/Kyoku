package com.poulastaa.auth.presentation.email.signup

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
class EmailSignUpViewModel @Inject constructor(
    private val validator: Validator,
) : ViewModel() {
    var state by mutableStateOf(EmailSignUpUiState())
        private set

    private val _uiEvent = Channel<EmailSignUpUiAction>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: EmailSignUpUiEvent) {
        when (event) {
            is EmailSignUpUiEvent.OnUserNameChange -> {

            }

            is EmailSignUpUiEvent.OnEmailChange -> {

            }

            EmailSignUpUiEvent.OnPasswordVisibilityToggle -> {
                state = state.copy(
                    isPasswordVisible = !state.isPasswordVisible
                )
            }

            is EmailSignUpUiEvent.OnPasswordChange -> {

            }

            is EmailSignUpUiEvent.OnConfirmPasswordChange -> {

            }

            EmailSignUpUiEvent.OnEmailLogInClick -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiEvent.send(EmailSignUpUiAction.OnEmailLogIn)
                }
            }

            EmailSignUpUiEvent.OnContinueClick -> {

            }
        }
    }
}