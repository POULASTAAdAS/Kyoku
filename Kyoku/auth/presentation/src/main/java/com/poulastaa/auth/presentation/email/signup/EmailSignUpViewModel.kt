package com.poulastaa.auth.presentation.email.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.auth.domain.PasswordState
import com.poulastaa.auth.domain.UsernameState
import com.poulastaa.auth.domain.Validator
import com.poulastaa.auth.domain.auth.AuthRepository
import com.poulastaa.auth.domain.auth.UserAuthStatus
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.ui.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmailSignUpViewModel @Inject constructor(
    private val validator: Validator,
    private val auth: AuthRepository,
) : ViewModel() {
    var state by mutableStateOf(EmailSignUpUiState())
        private set

    private val _uiEvent = Channel<EmailSignUpUiAction>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var emailSignUpVerificationJob: Job? = null

    fun onEvent(event: EmailSignUpUiEvent) {
        when (event) {
            is EmailSignUpUiEvent.OnUserNameChange -> {
                state = state.copy(
                    isValidUserName = validator.isValidUserName(event.value) == UsernameState.VALID,
                    userName = state.userName.copy(
                        data = event.value,
                        isErr = false,
                        errText = UiText.DynamicString("")
                    )
                )
            }

            is EmailSignUpUiEvent.OnEmailChange -> {
                state = state.copy(
                    isValidEmail = validator.isValidEmail(event.value),
                    email = state.email.copy(
                        data = event.value,
                        isErr = false,
                        errText = UiText.DynamicString("")
                    ),
                )
            }

            EmailSignUpUiEvent.OnPasswordVisibilityToggle -> {
                state = state.copy(
                    isPasswordVisible = !state.isPasswordVisible
                )
            }

            is EmailSignUpUiEvent.OnPasswordChange -> {
                state = state.copy(
                    password = state.password.copy(
                        data = event.value,
                        isErr = false,
                        errText = UiText.DynamicString("")
                    )
                )
            }

            is EmailSignUpUiEvent.OnConfirmPasswordChange -> {
                state = state.copy(
                    confirmPassword = state.confirmPassword.copy(
                        data = event.value,
                        isErr = false,
                        errText = UiText.DynamicString("")
                    )
                )
            }

            EmailSignUpUiEvent.OnEmailLogInClick -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiEvent.send(EmailSignUpUiAction.OnEmailLogIn)
                }
            }

            is EmailSignUpUiEvent.OnContinueClick -> {
                if (state.isMakingApiCall) return
                if (isErr()) return

                val countryCode = event.activity.resources.configuration.locales[0].country

                state = state.copy(
                    isMakingApiCall = true
                )

                viewModelScope.launch(Dispatchers.IO) {
                    val result = auth.emailSingUp(
                        email = state.email.data.trim(),
                        password = state.password.data.trim(),
                        username = state.userName.toString(),
                        countryCode = countryCode
                    )


                    when (result) {
                        is Result.Error -> {
                            when (result.error) {
                                DataError.Network.CONFLICT -> {
                                    state = state.copy(
                                        email = state.email.copy(
                                            isErr = true,
                                            errText = UiText.StringResource(R.string.error_email_already_exist)
                                        )
                                    )
                                }

                                DataError.Network.NO_INTERNET -> {
                                    _uiEvent.send(
                                        EmailSignUpUiAction.EmitToast(
                                            message = UiText.StringResource(R.string.error_no_internet)
                                        )
                                    )
                                }

                                else -> {
                                    _uiEvent.send(
                                        EmailSignUpUiAction.EmitToast(
                                            message = UiText.StringResource(R.string.error_something_went_wrong)
                                        )
                                    )
                                }
                            }

                            state = state.copy(
                                isMakingApiCall = false
                            )
                        }

                        is Result.Success -> {
                            when (result.data) {
                                UserAuthStatus.CREATED -> {
                                    _uiEvent.send(
                                        EmailSignUpUiAction.EmitToast(
                                            message = UiText.StringResource(R.string.verification_mail_sent)
                                        )
                                    )

                                    emailSignUpVerificationJob?.cancel()
                                    emailSignUpVerificationJob = signUpVerificationMailCheck()
                                }

                                else -> {
                                    _uiEvent.send(
                                        EmailSignUpUiAction.EmitToast(
                                            message = UiText.StringResource(R.string.error_something_went_wrong)
                                        )
                                    )

                                    state = state.copy(
                                        isMakingApiCall = false
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun isErr(): Boolean {
        var isErr = false

        if (!validator.isValidEmail(state.email.data.trim())) {
            state = state.copy(
                email = state.email.copy(
                    isErr = true,
                    errText = UiText.StringResource(R.string.error_invalid_email)
                )
            )

            isErr = true
        }

        val passwordState = validator.validatePassword(state.password.data.trim())
        if (passwordState != PasswordState.VALID) {
            state = state.copy(
                password = state.password.copy(
                    isErr = true
                )
            )

            isErr = true
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

        val username = validator.isValidUserName(state.userName.data.trim())
        if (username != UsernameState.VALID) {
            state = state.copy(
                isValidEmail = false,
                userName = state.userName.copy(
                    isErr = true
                )
            )

            isErr = true
        }
        when (username) {
            UsernameState.VALID -> Unit

            UsernameState.INVALID -> {
                state = state.copy(
                    userName = state.userName.copy(
                        errText = UiText.StringResource(R.string.error_username_invalid)
                    )
                )
            }

            UsernameState.INVALID_START_WITH_UNDERSCORE -> {
                state = state.copy(
                    userName = state.userName.copy(
                        errText = UiText.StringResource(R.string.error_username_cant_start_with_underscore)
                    )
                )
            }

            UsernameState.EMPTY -> {
                state = state.copy(
                    userName = state.userName.copy(
                        errText = UiText.StringResource(R.string.error_username_empty)
                    )
                )
            }

            UsernameState.TOO_LONG -> {
                state = state.copy(
                    userName = state.userName.copy(
                        errText = UiText.StringResource(R.string.error_username_to_long)
                    )
                )
            }
        }

        if (state.password.data.trim() != state.confirmPassword.data.trim()) {
            state = state.copy(
                confirmPassword = state.confirmPassword.copy(
                    isErr = true,
                    errText = UiText.StringResource(R.string.error_password_not_match)
                ),
                password = state.password.copy(
                    isErr = true,
                    errText = UiText.StringResource(R.string.error_password_not_match)
                )
            )

            isErr = true
        }

        return isErr
    }

    private fun signUpVerificationMailCheck() = viewModelScope.launch(Dispatchers.IO) {


        state = state.copy(
            isMakingApiCall = false
        )
    }
}