package com.poulastaa.auth.presentation.email.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.auth.domain.PasswordState
import com.poulastaa.auth.domain.Validator
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.ui.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmailLoginViewModel @Inject constructor(
    private val validator: Validator,
) : ViewModel() {
    var state by mutableStateOf(EmailLogInUiState())
        private set

    private val _uiEvent = Channel<EmailLoginUiAction>()
    val uiAction = _uiEvent.receiveAsFlow()

    private var resendVerificationMailJob: Job? = null

    fun onEvent(event: EmailLoginUiEvent) {
        when (event) {
            is EmailLoginUiEvent.OnEmailChange -> {
                state = state.copy(
                    isValidEmail = validator.isValidEmail(event.value),
                    email = state.email.copy(
                        data = event.value,
                        isErr = false,
                        errText = UiText.DynamicString("")
                    ),
                )
            }

            is EmailLoginUiEvent.OnPasswordChange -> {
                state = state.copy(
                    password = state.password.copy(
                        data = event.value,
                        isErr = false,
                        errText = UiText.DynamicString("")
                    )
                )
            }

            EmailLoginUiEvent.OnForgotPasswordClick -> {
                viewModelScope.launch {
                    _uiEvent.send(EmailLoginUiAction.OnForgotPassword(email = state.email.data))
                }
            }

            EmailLoginUiEvent.OnEmailSignUpClick -> {
                state = state.copy(
                    isMakingApiCall = false,

                    canResendMailAgain = false,
                    resendMailText = "40",
                    isResendVerificationMailVisible = false,
                    isResendMailLoading = false
                )

                resendVerificationMailJob?.cancel()

                viewModelScope.launch {
                    _uiEvent.send(EmailLoginUiAction.OnEmailSignUp)
                }
            }

            EmailLoginUiEvent.OnContinueClick -> {
                if (state.isMakingApiCall) return
                if (isErr()) return

                state = state.copy(
                    isMakingApiCall = true
                )
                resendVerificationMailJob?.cancel()


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

    private fun isErr(): Boolean {
        var err = false

        if (!validator.isValidEmail(state.email.data.trim())) {
            state = state.copy(
                email = state.email.copy(
                    isErr = true,
                    errText = UiText.StringResource(R.string.error_invalid_email)
                )
            )

            err = true
        }

        val passwordState = validator.validatePassword(state.password.data.trim())

        if (passwordState != PasswordState.VALID) {
            state = state.copy(
                password = state.password.copy(
                    isErr = true
                )
            )

            err = true
        }

        when (passwordState) {
            PasswordState.VALID -> Unit

            PasswordState.EMPTY -> {
                state = state.copy(
                    password = state.password.copy(
                        errText = UiText.StringResource(R.string.error_empty_password)
                    )
                )
            }

            PasswordState.TOO_SHORT -> {
                state = state.copy(
                    password = state.password.copy(
                        errText = UiText.StringResource(R.string.error_password_to_short)
                    )
                )
            }

            PasswordState.TOO_LONG -> {
                state = state.copy(
                    password = state.password.copy(
                        errText = UiText.StringResource(R.string.error_password_to_long)
                    )
                )
            }

            PasswordState.INVALID -> {
                state = state.copy(
                    password = state.password.copy(
                        errText = UiText.StringResource(R.string.error_password_invalid)
                    )
                )
            }
        }

        return err
    }
}