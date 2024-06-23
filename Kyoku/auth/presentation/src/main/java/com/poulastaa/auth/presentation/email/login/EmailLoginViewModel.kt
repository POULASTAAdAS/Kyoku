package com.poulastaa.auth.presentation.email.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.auth.domain.PasswordState
import com.poulastaa.auth.domain.Validator
import com.poulastaa.auth.domain.auth.AuthRepository
import com.poulastaa.auth.domain.auth.UserAuthStatus
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.ScreenEnum
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.ui.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmailLoginViewModel @Inject constructor(
    private val validator: Validator,
    private val auth: AuthRepository,
    private val ds: DataStoreRepository,
) : ViewModel() {
    var state by mutableStateOf(EmailLogInUiState())
        private set

    private val _uiEvent = Channel<EmailLoginUiAction>()
    val uiAction = _uiEvent.receiveAsFlow()

    private var emailSignInVerificationJob: Job? = null
    private var emailLogInVerificationJob: Job? = null
    private var resendMailJob: Job? = null

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
                    resendMailText = "40 s",
                    isResendVerificationMailVisible = false,
                    isResendMailLoading = false
                )

                resendMailJob?.cancel()
                emailLogInVerificationJob?.cancel()
                emailSignInVerificationJob?.cancel()

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

    private fun logInUser() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = auth.emailLogIn(
                email = state.email.data.trim(),
                password = state.password.data.trim()
            )

            when (result) {
                is Result.Error -> {
                    when (result.error) {
                        DataError.Network.NO_INTERNET -> {
                            _uiEvent.send(
                                EmailLoginUiAction.EmitToast(
                                    message = UiText.StringResource(R.string.error_no_internet)
                                )
                            )
                        }

                        DataError.Network.NOT_FOUND -> {
                            state = state.copy(
                                email = state.email.copy(
                                    isErr = true,
                                    errText = UiText.StringResource(R.string.error_user_not_found)
                                )
                            )
                        }

                        DataError.Network.PASSWORD_DOES_NOT_MATCH -> {
                            state = state.copy(
                                password = state.password.copy(
                                    isErr = true,
                                    errText = UiText.StringResource(R.string.password)
                                )
                            )
                        }

                        DataError.Network.EMAIL_NOT_VERIFIED -> {
                            _uiEvent.send(
                                EmailLoginUiAction.EmitToast(
                                    message = UiText.StringResource(R.string.verification_mail_sent)
                                )
                            )

                            emailSignInVerificationJob?.cancel()
                            emailSignInVerificationJob = emailSignUpVerificationCheck()

                            return@launch
                        }

                        else -> {
                            _uiEvent.send(
                                EmailLoginUiAction.EmitToast(
                                    message = UiText.StringResource(R.string.error_something_went_wrong)
                                )
                            )
                        }
                    }.let {
                        state = state.copy(
                            isMakingApiCall = false
                        )
                    }
                }

                is Result.Success -> {
                    when (result.data) {
                        UserAuthStatus.CREATED -> {
                            ScreenEnum.GET_SPOTIFY_PLAYLIST
                        }

                        UserAuthStatus.USER_FOUND_HOME -> {
                            ScreenEnum.HOME
                        }

                        UserAuthStatus.USER_FOUND_STORE_B_DATE -> {
                            ScreenEnum.SET_B_DATE
                        }

                        UserAuthStatus.USER_FOUND_SET_GENRE -> {
                            ScreenEnum.PIC_GENRE
                        }

                        UserAuthStatus.USER_FOUND_SET_ARTIST -> {
                            ScreenEnum.PIC_ARTIST
                        }

                        else -> {
                            _uiEvent.send(
                                EmailLoginUiAction.EmitToast(
                                    message = UiText.StringResource(R.string.error_something_went_wrong)
                                )
                            )
                            return@launch
                        }
                    }.let {
                        emailLogInVerificationJob?.cancel()
                        emailSignInVerificationJob = emailLogInVerificationCheck(it)

                        delay(10_000L)
                        resendMailJob?.cancel()
                        resendMailJob = resendVerificationMailCounter()
                    }
                }
            }
        }
    }

    private fun emailSignUpVerificationCheck() = viewModelScope.launch(Dispatchers.IO) {
        // todo

        state = state.copy(
            isMakingApiCall = false
        )

        ds.storeSignInState(ScreenEnum.GET_SPOTIFY_PLAYLIST)
        _uiEvent.send(EmailLoginUiAction.OnSuccess(ScreenEnum.GET_SPOTIFY_PLAYLIST))
    }

    private fun emailLogInVerificationCheck(
        screen: ScreenEnum,
    ) = viewModelScope.launch(Dispatchers.IO) {
        // todo

        state = state.copy(
            isMakingApiCall = false
        )

        ds.storeSignInState(screen)
        _uiEvent.send(EmailLoginUiAction.OnSuccess(screen))
    }

    private fun resendVerificationMailCounter() = viewModelScope.launch(Dispatchers.IO) {
        for (i in 39 downTo 1) {
            delay(1000L)

            state = state.copy(
                resendMailText = "$i s"
            )
        }

        state = state.copy(
            canResendMailAgain = true,
            isResendMailLoading = false,
            resendMailText = "Send Again"
        )
    }
}