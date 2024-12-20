package com.poulastaa.auth.presentation.email.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.auth.domain.AuthValidator
import com.poulastaa.auth.domain.model.PasswordState
import com.poulastaa.auth.domain.model.UsernameState
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
class EmailSignUpViewModel @Inject constructor(
    private val validator: AuthValidator,
) : ViewModel() {
    private val _state = MutableStateFlow(EmailSignUpUiState())
    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = EmailSignUpUiState()
        )

    private val _uiEvent = Channel<EmailSignUpUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onAction(action: EmailSignUpUiAction) {
        when (action) {
            is EmailSignUpUiAction.OnUsernameChange -> {
                _state.update {
                    it.copy(
                        username = it.email.copy(
                            value = action.username,
                            isErr = false,
                            isValid = validator.isValidEmail(action.username),
                            errText = UiText.DynamicString("")
                        )
                    )
                }
            }

            is EmailSignUpUiAction.OnEmailChange -> {
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

            is EmailSignUpUiAction.OnPasswordChange -> {
                _state.update {
                    it.copy(
                        password = it.email.copy(
                            value = action.password,
                            isErr = false,
                            isValid = validator.isValidEmail(action.password),
                            errText = UiText.DynamicString("")
                        )
                    )
                }
            }

            is EmailSignUpUiAction.OnConformPasswordChange -> {
                _state.update {
                    it.copy(
                        conformPassword = it.email.copy(
                            value = action.password,
                            isErr = false,
                            isValid = validator.isValidEmail(action.password),
                            errText = UiText.DynamicString("")
                        )
                    )
                }
            }

            is EmailSignUpUiAction.OnConformClick -> {
                if (_state.value.isMakingApiCall) return
                if (isErr()) return

                _state.update {
                    it.copy(
                        isMakingApiCall = true
                    )
                }
            }

            EmailSignUpUiAction.OnEmailLogInClick -> {
                viewModelScope.launch {
                    _uiEvent.send(EmailSignUpUiEvent.NavigateToLogIn)
                }
            }

            EmailSignUpUiAction.OnPasswordVisibilityToggle -> {
                _state.update {
                    it.copy(
                        isPasswordVisible = !it.isPasswordVisible
                    )
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

            PasswordState.EMPTY -> _state.update {
                it.copy(
                    password = it.password.copy(
                        errText = UiText.StringResource(R.string.error_empty_password)
                    )
                )
            }

            PasswordState.TOO_SHORT -> _state.update {
                it.copy(
                    password = it.password.copy(
                        errText = UiText.StringResource(R.string.error_password_to_short)
                    )
                )
            }

            PasswordState.TOO_LONG -> _state.update {
                it.copy(
                    password = it.password.copy(
                        errText = UiText.StringResource(R.string.error_password_to_long)
                    )
                )
            }

            PasswordState.INVALID -> _state.update {
                it.copy(
                    password = it.password.copy(
                        errText = UiText.StringResource(R.string.error_password_invalid)
                    )
                )
            }
        }

        val username = validator.isValidUserName(_state.value.username.value.trim())
        if (username != UsernameState.VALID) {
            _state.update {
                it.copy(
                    username = it.username.copy(
                        isErr = true
                    )
                )
            }

            err = true
        }

        when (username) {
            UsernameState.VALID -> Unit

            UsernameState.INVALID -> _state.update {
                it.copy(
                    username = it.username.copy(
                        errText = UiText.StringResource(R.string.error_username_invalid)
                    )
                )
            }

            UsernameState.INVALID_START_WITH_UNDERSCORE -> _state.update {
                it.copy(
                    username = it.username.copy(
                        errText = UiText.StringResource(R.string.error_username_cant_start_with_underscore)
                    )
                )
            }

            UsernameState.EMPTY -> _state.update {
                it.copy(
                    username = it.username.copy(
                        errText = UiText.StringResource(R.string.error_username_empty)
                    )
                )
            }

            UsernameState.TOO_LONG -> _state.update {
                it.copy(
                    username = it.username.copy(
                        errText = UiText.StringResource(R.string.error_username_to_long)
                    )
                )
            }
        }

        if (_state.value.password.value.trim() != _state.value.conformPassword.value.trim()) {
            _state.update {
                it.copy(
                    conformPassword = it.conformPassword.copy(
                        isErr = true,
                        errText = UiText.StringResource(R.string.error_password_not_match)
                    ),
                    password = it.password.copy(
                        isErr = true,
                        errText = UiText.StringResource(R.string.error_password_not_match)
                    )
                )
            }

            err = true
        }

        return err
    }
}