package com.poulastaa.auth.presentation.email.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.auth.domain.AuthRepository
import com.poulastaa.auth.domain.AuthValidator
import com.poulastaa.auth.domain.model.AuthStatus
import com.poulastaa.auth.domain.model.PasskeyPayload
import com.poulastaa.auth.domain.model.PasswordState
import com.poulastaa.auth.domain.model.UsernameState
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.model.SavedScreen
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.ui.UiText
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
class EmailSignUpViewModel @Inject constructor(
    private val validator: AuthValidator,
    private val repo: AuthRepository,
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

    private var emailVerificationJob: Job? = null

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

                viewModelScope.launch {
                    val result = repo.emailSignUp(
                        email = _state.value.email.value.trim(),
                        password = _state.value.password.value.trim(),
                        username = _state.value.username.value.trim(),
                        countryCode = action.countryCode
                    )

                    when (result) {
                        is Result.Error -> {
                            when (result.error) {
                                DataError.Network.EMAIL_NOT_VERIFIED -> _uiEvent.send(
                                    EmailSignUpUiEvent.EmitToast(
                                        UiText.StringResource(
                                            R.string.verification_mail_sent
                                        )
                                    )
                                )

                                DataError.Network.INVALID_EMAIL -> {
                                    _uiEvent.send(
                                        EmailSignUpUiEvent.EmitToast(
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

                                DataError.Network.CONFLICT -> {
                                    _uiEvent.send(
                                        EmailSignUpUiEvent.EmitToast(
                                            UiText.StringResource(
                                                R.string.error_email_already_exist
                                            )
                                        )
                                    )

                                    _state.update {
                                        it.copy(
                                            email = it.email.copy(
                                                isErr = true,
                                                errText = UiText.StringResource(R.string.error_email_already_exist)
                                            )
                                        )
                                    }
                                }

                                DataError.Network.NO_INTERNET -> _uiEvent.send(
                                    EmailSignUpUiEvent.EmitToast(
                                        UiText.StringResource(
                                            R.string.error_no_internet
                                        )
                                    )
                                )

                                else -> _uiEvent.send(
                                    EmailSignUpUiEvent.EmitToast(
                                        UiText.StringResource(
                                            R.string.error_something_went_wrong
                                        )
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
                                    EmailSignUpUiEvent.EmitToast(
                                        UiText.StringResource(
                                            R.string.verification_mail_sent
                                        )
                                    )
                                )

                                AuthStatus.EMAIL_ALREADY_IN_USE -> {
                                    _uiEvent.send(
                                        EmailSignUpUiEvent.EmitToast(
                                            UiText.StringResource(
                                                R.string.error_email_already_exist
                                            )
                                        )
                                    )

                                    _state.update {
                                        it.copy(
                                            email = it.email.copy(
                                                isErr = true,
                                                errText = UiText.StringResource(R.string.error_email_already_exist)
                                            )
                                        )
                                    }
                                }

                                AuthStatus.INVALID_EMAIL -> {
                                    _uiEvent.send(
                                        EmailSignUpUiEvent.EmitToast(
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

                                else -> _uiEvent.send(
                                    EmailSignUpUiEvent.EmitToast(
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

            EmailSignUpUiAction.OnCreatePasskey -> {
//                viewModelScope.launch { // todo can only be implemented for https request
//                    val result = repo.getPasskeyRequest(_state.value.email.value.trim())
//
//                    when (result.type) {
//                        PasskeyType.LOGIN -> validatePasskey(result)
//                        PasskeyType.SIGNUP -> createPasskey(result)
//                    }
//                }

                onAction(EmailSignUpUiAction.OnCancelPasskeyCreation)
            }

            EmailSignUpUiAction.OnCancelPasskeyCreation -> {
                _state.update {
                    it.copy(
                        isPasskeyCreatePopUp = false
                    )
                }

                viewModelScope.launch {
                    delay(300)
                    _uiEvent.send(EmailSignUpUiEvent.OnSuccessNavigate(SavedScreen.IMPORT_SPOTIFY_PLAYLIST))
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

    private fun startVerificationMailJob(email: String, authState: AuthStatus) =
        viewModelScope.launch {
            for (i in 1..15) { // 45 seconds
                delay(3000)

                val result = repo.checkEmailVerificationState(email, authState)

                if (result is Result.Success && result.data) {
                    if (authState == AuthStatus.CREATED) _uiEvent.send(
                        EmailSignUpUiEvent.EmitToast(
                            UiText.StringResource(R.string.welcome)
                        )
                    ) else _uiEvent.send(
                        EmailSignUpUiEvent.EmitToast(
                            UiText.StringResource(R.string.welcome_back)
                        )
                    )

                    return@launch when (authState) {
                        AuthStatus.CREATED, AuthStatus.USER_FOUND -> _state.update {
                            it.copy(
                                isPasskeyCreatePopUp = true
                            )
                        }

                        AuthStatus.USER_FOUND_STORE_B_DATE -> _uiEvent.send(
                            EmailSignUpUiEvent.OnSuccessNavigate(SavedScreen.SET_B_DATE)
                        )

                        AuthStatus.USER_FOUND_SET_GENRE -> _uiEvent.send(
                            EmailSignUpUiEvent.OnSuccessNavigate(SavedScreen.PIC_GENRE)
                        )

                        AuthStatus.USER_FOUND_SET_ARTIST -> _uiEvent.send(
                            EmailSignUpUiEvent.OnSuccessNavigate(SavedScreen.PIC_ARTIST)
                        )

                        AuthStatus.USER_FOUND_HOME -> _uiEvent.send(
                            EmailSignUpUiEvent.OnSuccessNavigate(SavedScreen.HOME)
                        )

                        else -> Unit
                    }
                }
            }
        }

    private fun createPasskey(payload: PasskeyPayload) {

    }

    private fun validatePasskey(payload: PasskeyPayload) {

    }
}