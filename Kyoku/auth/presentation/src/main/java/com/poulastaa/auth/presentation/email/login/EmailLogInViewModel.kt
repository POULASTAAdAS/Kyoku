package com.poulastaa.auth.presentation.email.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.auth.domain.AuthRepository
import com.poulastaa.auth.domain.AuthValidator
import com.poulastaa.auth.domain.model.AuthStatus
import com.poulastaa.auth.domain.model.PasswordState
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.model.SavedScreen
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmailLogInViewModel @Inject constructor(
    private val validator: AuthValidator,
    private val repo: AuthRepository,
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

    private var emailVerificationJob: Job? = null

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
                        isMakingApiCall = true,
                    )
                }

                viewModelScope.launch {
                    val result = repo.emailLogIn(
                        email = _state.value.email.value.trim(),
                        password = _state.value.password.value.trim()
                    )

                    when (result) {
                        is Result.Error -> {
                            when (result.error) {
                                DataError.Network.EMAIL_NOT_VERIFIED -> {
                                    _uiEvent.send(
                                        EmailLogInUiEvent.EmitToast(
                                            UiText.StringResource(R.string.verification_mail_sent)
                                        )
                                    )

                                    startVerificationMailJob(
                                        _state.value.email.value.trim(),
                                        AuthStatus.USER_FOUND
                                    )
                                }

                                DataError.Network.PASSWORD_DOES_NOT_MATCH -> {
                                    _uiEvent.send(
                                        EmailLogInUiEvent.EmitToast(
                                            UiText.StringResource(R.string.error_password_does_not_match)
                                        )
                                    )

                                    _state.update {
                                        it.copy(
                                            password = it.password.copy(
                                                isErr = true,
                                                errText = UiText.StringResource(R.string.error_password_does_not_match)
                                            )
                                        )
                                    }
                                }

                                DataError.Network.NOT_FOUND -> {
                                    _uiEvent.send(
                                        EmailLogInUiEvent.EmitToast(
                                            UiText.StringResource(R.string.error_user_not_found)
                                        )
                                    )

                                    _state.update {
                                        it.copy(
                                            email = it.email.copy(
                                                isErr = true,
                                                errText = UiText.StringResource(R.string.error_user_not_found)
                                            )
                                        )
                                    }
                                }

                                DataError.Network.INVALID_EMAIL -> {
                                    _uiEvent.send(
                                        EmailLogInUiEvent.EmitToast(
                                            UiText.StringResource(R.string.error_invalid_email)
                                        )
                                    )

                                    _state.update {
                                        it.copy(
                                            email = it.email.copy(
                                                isErr = true,
                                                errText = UiText.StringResource(R.string.error_invalid_email)
                                            )
                                        )
                                    }
                                }

                                DataError.Network.NO_INTERNET -> {
                                    _uiEvent.send(
                                        EmailLogInUiEvent.EmitToast(
                                            UiText.StringResource(R.string.error_no_internet)
                                        )
                                    )

                                    _state.update {
                                        it.copy(
                                            email = it.email.copy(
                                                isErr = true,
                                                errText = UiText.StringResource(R.string.error_no_internet)
                                            )
                                        )
                                    }
                                }

                                else -> _uiEvent.send(
                                    EmailLogInUiEvent.EmitToast(
                                        UiText.StringResource(R.string.error_something_went_wrong)
                                    )
                                )
                            }

                            _state.update {
                                it.copy(
                                    isMakingApiCall = false
                                )
                            }
                        }

                        is Result.Success -> {
                            when (result.data) {
                                AuthStatus.CREATED,
                                AuthStatus.USER_FOUND,
                                AuthStatus.USER_FOUND_STORE_B_DATE,
                                AuthStatus.USER_FOUND_SET_GENRE,
                                AuthStatus.USER_FOUND_SET_ARTIST,
                                AuthStatus.USER_FOUND_HOME,
                                AuthStatus.EMAIL_NOT_VERIFIED,
                                    -> _uiEvent.send(
                                    EmailLogInUiEvent.EmitToast(
                                        UiText.StringResource(
                                            R.string.verification_mail_sent
                                        )
                                    )
                                )

                                AuthStatus.INVALID_EMAIL -> {
                                    _uiEvent.send(
                                        EmailLogInUiEvent.EmitToast(
                                            UiText.StringResource(
                                                R.string.error_invalid_email
                                            )
                                        )
                                    )

                                    _state.update {
                                        it.copy(
                                            email = it.email.copy(
                                                isErr = true,
                                                errText = UiText.StringResource(R.string.error_invalid_email)
                                            )
                                        )
                                    }
                                }

                                AuthStatus.USER_NOT_FOUND -> {
                                    _uiEvent.send(
                                        EmailLogInUiEvent.EmitToast(
                                            UiText.StringResource(
                                                R.string.error_user_not_found
                                            )
                                        )
                                    )

                                    _state.update {
                                        it.copy(
                                            email = it.email.copy(
                                                isErr = true,
                                                errText = UiText.StringResource(R.string.error_user_not_found)
                                            )
                                        )
                                    }
                                }

                                AuthStatus.PASSWORD_DOES_NOT_MATCH -> {
                                    _uiEvent.send(
                                        EmailLogInUiEvent.EmitToast(
                                            UiText.StringResource(
                                                R.string.error_password_does_not_match
                                            )
                                        )
                                    )

                                    _state.update {
                                        it.copy(
                                            password = it.password.copy(
                                                isErr = true,
                                                errText = UiText.StringResource(R.string.error_password_does_not_match)
                                            )
                                        )
                                    }
                                }

                                else -> _uiEvent.send(
                                    EmailLogInUiEvent.EmitToast(
                                        UiText.StringResource(
                                            R.string.error_something_went_wrong
                                        )
                                    )
                                )
                            }

                            if (result.data == AuthStatus.CREATED ||
                                result.data == AuthStatus.USER_FOUND ||
                                result.data == AuthStatus.USER_FOUND_STORE_B_DATE ||
                                result.data == AuthStatus.USER_FOUND_SET_GENRE ||
                                result.data == AuthStatus.USER_FOUND_SET_ARTIST ||
                                result.data == AuthStatus.USER_FOUND_HOME ||
                                result.data == AuthStatus.EMAIL_NOT_VERIFIED
                            ) {
                                emailVerificationJob?.cancel()
                                emailVerificationJob = startVerificationMailJob(
                                    email = _state.value.email.value.trim(),
                                    authState = result.data
                                )
                            } else _state.update {
                                it.copy(
                                    isMakingApiCall = false
                                )
                            }
                        }
                    }
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

    private fun startVerificationMailJob(email: String, authState: AuthStatus) =
        viewModelScope.launch {
            for (i in 1..40) { // 120 seconds
                delay(3000)

                val result = repo.checkEmailVerificationState(email, authState)

                if (result is Result.Success && result.data) {
                    if (authState == AuthStatus.CREATED) _uiEvent.send(
                        EmailLogInUiEvent.EmitToast(
                            UiText.StringResource(R.string.welcome)
                        )
                    ) else _uiEvent.send(
                        EmailLogInUiEvent.EmitToast(
                            UiText.StringResource(R.string.welcome_back)
                        )
                    )

                    when (authState) {
                        AuthStatus.CREATED -> {
                            _uiEvent.send(
                                EmailLogInUiEvent.OnSuccess(SavedScreen.IMPORT_SPOTIFY_PLAYLIST)
                            )
                        }

                        AuthStatus.USER_FOUND_STORE_B_DATE -> _uiEvent.send(
                            EmailLogInUiEvent.OnSuccess(SavedScreen.SET_B_DATE)
                        )

                        AuthStatus.USER_FOUND_SET_GENRE -> _uiEvent.send(
                            EmailLogInUiEvent.OnSuccess(SavedScreen.PIC_GENRE)
                        )

                        AuthStatus.USER_FOUND_SET_ARTIST -> _uiEvent.send(
                            EmailLogInUiEvent.OnSuccess(SavedScreen.PIC_ARTIST)
                        )

                        AuthStatus.USER_FOUND, AuthStatus.USER_FOUND_HOME -> _uiEvent.send(
                            EmailLogInUiEvent.OnSuccess(SavedScreen.MAIN)
                        )

                        else -> return@launch
                    }
                }
            }
        }
}