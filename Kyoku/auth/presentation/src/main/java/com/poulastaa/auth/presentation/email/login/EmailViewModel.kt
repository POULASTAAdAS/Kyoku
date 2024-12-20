package com.poulastaa.auth.presentation.email.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.auth.domain.AuthValidator
import com.poulastaa.auth.domain.model.PasswordState
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.ui.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmailViewModel @Inject constructor(
    private val validator: AuthValidator,
) : ViewModel() {
    private val _state = MutableStateFlow(EmailLoginUiState())
    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = EmailLoginUiState()
        )

    private val _uiEvent = Channel<EmailLogInUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onAction(action: EmailLogInUiAction) {
        when (action) {
            is EmailLogInUiAction.OnEmailChange -> {
                _state.update {
                    it.copy(
                        email = it.email.copy(
                            value = action.email,
                            isErr = false,
                            isValid = validator.isValidEmail(action.email),
                            errText = UiText.DynamicString("")
                        )
                    )
                }
            }

            is EmailLogInUiAction.OnPasswordChange -> {
                _state.update {
                    it.copy(
                        password = it.password.copy(
                            value = action.password,
                            isErr = false,
                            errText = UiText.DynamicString("")
                        )
                    )
                }
            }

            EmailLogInUiAction.OnPasswordVisibilityToggle -> {
                _state.update {
                    it.copy(
                        isPasswordVisible = !it.isPasswordVisible
                    )
                }
            }

            EmailLogInUiAction.OnEmailSignUpClick -> {
                _state.update {
                    it.copy(
                        isMakingApiCall = false,
                    )
                }

                viewModelScope.launch {
                    _uiEvent.send(EmailLogInUiEvent.NavigateToSignUp)
                }
            }

            EmailLogInUiAction.OnConformClick -> {
                if (_state.value.isMakingApiCall) return
                if (isErr()) return

                _state.update {
                    it.copy(
                        isMakingApiCall = false,
                    )
                }

                _state.update {
                    it.copy(
                        isMakingApiCall = true
                    )
                }
            }

            EmailLogInUiAction.OnForgotPasswordClick -> {
                viewModelScope.launch {
                    _uiEvent.send(EmailLogInUiEvent.NavigateToForgotPassword(_state.value.email.value.trim()))
                }
            }
        }
    }

    private fun isErr(): Boolean {
        var err = false

        if (!validator.isValidEmail(_state.value.email.value.trim())) {
            _state.update {
                it.copy(
                    email = it.email.copy(
                        isErr = true,
                        errText = UiText.StringResource(R.string.error_invalid_email)
                    )
                )
            }


            err = true
        }

        val passwordState = validator.validatePassword(_state.value.password.value.trim())

        if (passwordState != PasswordState.VALID) {
            _state.update {
                it.copy(
                    password = it.password.copy(
                        isErr = true
                    )
                )
            }

            err = true
        }

        when (passwordState) {
            PasswordState.VALID -> Unit

            PasswordState.EMPTY -> {
                _state.update {
                    it.copy(
                        password = it.password.copy(
                            errText = UiText.StringResource(R.string.error_empty_password)
                        )
                    )
                }
            }

            PasswordState.TOO_SHORT -> {
                _state.update {
                    it.copy(
                        password = it.password.copy(
                            errText = UiText.StringResource(R.string.error_password_to_short)
                        )
                    )
                }
            }

            PasswordState.TOO_LONG -> {
                _state.update {
                    it.copy(
                        password = it.password.copy(
                            errText = UiText.StringResource(R.string.error_password_to_long)
                        )
                    )
                }
            }

            PasswordState.INVALID -> {
                _state.update {
                    it.copy(
                        password = it.password.copy(
                            errText = UiText.StringResource(R.string.error_password_invalid)
                        )
                    )
                }
            }
        }

        return err
    }
}