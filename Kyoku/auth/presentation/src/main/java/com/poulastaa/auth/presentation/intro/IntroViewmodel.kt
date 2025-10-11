package com.poulastaa.auth.presentation.intro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.auth.domain.AuthValidator
import com.poulastaa.auth.domain.IntroRepository
import com.poulastaa.auth.domain.model.PasswordStatus
import com.poulastaa.auth.domain.model.response.DtoResponseStatus
import com.poulastaa.auth.presentation.R
import com.poulastaa.auth.presentation.intro.model.IntroAllowedNavigationScreens
import com.poulastaa.auth.presentation.intro.model.IntroUiState
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.presentation.designsystem.TextProp
import com.poulastaa.core.presentation.designsystem.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.poulastaa.core.presentation.ui.R as CoreR

@HiltViewModel
internal class IntroViewmodel @Inject constructor(
    private val validator: AuthValidator,
    private val repo: IntroRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(IntroUiState())
    val state = _state.onStart {
        observeFailedAttemptCount()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = IntroUiState()
    )

    private val _uiEvent = Channel<IntroUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var emailLogInJob: Job? = null
    private var logInAttempt = MutableStateFlow(0)

    override fun onCleared() {
        super.onCleared()
        emailLogInJob?.cancel()
    }

    fun onAction(action: IntroUiAction) {
        if ((_state.value.isGoogleAuthLoading || _state.value.isEmailAuthLoading) &&
            action != IntroUiAction.ObPasswordVisibilityToggle
        ) return

        when (action) {
            is IntroUiAction.OnEmailChange -> {
                _state.update {
                    it.copy(
                        email = it.email.copy(
                            isValidEmail = validator.isValidEmail(action.email.trim()),
                            prop = TextProp(action.email.trim())
                        )
                    )
                }
            }

            is IntroUiAction.OnPasswordChange -> {
                _state.update {
                    it.copy(
                        password = it.password.copy(
                            prop = TextProp(action.password.trim())
                        )
                    )
                }
            }

            IntroUiAction.ObPasswordVisibilityToggle -> {
                _state.update {
                    it.copy(
                        password = it.password.copy(
                            isPasswordVisible = it.password.isPasswordVisible.not()
                        )
                    )
                }
            }

            IntroUiAction.OnForgotPasswordClick -> viewModelScope.launch {
                _uiEvent.send(
                    IntroUiEvent.Navigate(
                        IntroAllowedNavigationScreens.ForgotPassword(
                            email = if (validator.isValidEmail(_state.value.email.prop.value)) _state.value.email.prop.value
                            else null
                        )
                    )
                )
            }

            IntroUiAction.OnEmailSubmit -> {
                if (validate().not()) return

                _state.update {
                    it.copy(
                        isEmailAuthLoading = true
                    )
                }

                logInAttempt.update { it + 1 }

                viewModelScope.launch {
                    delay(2_000) // simulate network delay

                    when (val res = repo.emailLogIn(
                        _state.value.email.prop.value.trim(),
                        _state.value.password.prop.value.trim()
                    )) {
                        is Result.Error -> when (res.error) {
                            DataError.Network.NO_INTERNET -> _uiEvent.trySend(
                                IntroUiEvent.EmitToast(
                                    UiText.StringResource(CoreR.string.please_check_internet_connection)
                                )
                            )

                            DataError.Network.SERVER_ERROR -> _uiEvent.trySend(
                                IntroUiEvent.EmitToast(
                                    UiText.StringResource(CoreR.string.something_went_wrong_try_again)
                                )
                            )

                            else -> _uiEvent.trySend(
                                IntroUiEvent.EmitToast(
                                    UiText.StringResource(CoreR.string.something_went_wrong_try_again)
                                )
                            )
                        }

                        is Result.Success -> when (res.data) {
                            DtoResponseStatus.USER_CREATED, DtoResponseStatus.USER_FOUND,
                            DtoResponseStatus.USER_FOUND_NO_PLAYLIST, DtoResponseStatus.USER_FOUND_NO_ARTIST,
                            DtoResponseStatus.USER_FOUND_NO_GENRE, DtoResponseStatus.USER_FOUND_NO_B_DATE,
                                -> {
                                emailLogInJob?.cancel()
                                emailLogInJob = startEmailValidationJob()
                            }

                            DtoResponseStatus.EMAIL_NOT_VALID -> _state.update {
                                it.copy(
                                    email = it.email.copy(
                                        isValidEmail = false,
                                        prop = it.email.prop.copy(
                                            isErr = true,
                                            errText = UiText.StringResource(R.string.invalid_email)
                                        )
                                    )
                                )
                            }

                            DtoResponseStatus.PASSWORD_DOES_NOT_MATCH -> _state.update {
                                it.copy(
                                    password = it.password.copy(
                                        prop = it.password.prop.copy(
                                            isErr = true,
                                            errText = UiText.StringResource(R.string.password_does_not_match)
                                        )
                                    )
                                )
                            }

                            DtoResponseStatus.USER_NOT_FOUND -> _state.update {
                                it.copy(
                                    isNewEmailUser = true,
                                    email = it.email.copy(
                                        prop = it.email.prop.copy(
                                            isErr = true,
                                            errText = UiText.StringResource(CoreR.string.email_not_registered)
                                        )
                                    )
                                )
                            }

                            else -> _uiEvent.trySend(
                                IntroUiEvent.EmitToast(
                                    UiText.StringResource(CoreR.string.something_went_wrong_try_again)
                                )
                            )
                        }
                    }

                    _state.update {
                        it.copy(isEmailAuthLoading = false)
                    }
                }
            }

            IntroUiAction.OnEmailSingUpClick -> viewModelScope.launch {
                _uiEvent.send(
                    IntroUiEvent.Navigate(
                        IntroAllowedNavigationScreens.SingUp(
                            email = if (validator.isValidEmail(_state.value.email.prop.value)) _state.value.email.prop.value
                            else null
                        )
                    )
                )
            }

            IntroUiAction.OnGoogleAuthClick -> {
                _state.update {
                    it.copy(
                        isGoogleAuthLoading = true
                    )
                }
            }
        }
    }

    private fun validate(): Boolean {
        var isValidEmail = true
        var isValidPassword = false

        if (validator.isValidEmail(_state.value.email.prop.value).not()) {
            _state.update {
                it.copy(
                    email = it.email.copy(
                        prop = it.email.prop.copy(
                            isErr = true,
                            errText = UiText.StringResource(R.string.invalid_email)
                        )
                    )
                )
            }

            isValidEmail = false
        }

        val passwordState = validator.validatePassword(_state.value.password.prop.value)

        _state.update {
            it.copy(
                password = it.password.copy(
                    prop = when (passwordState) {
                        PasswordStatus.VALID -> it.password.prop.copy(
                            isErr = false,
                            errText = UiText.DynamicString("")
                        ).also { isValidPassword = true }

                        PasswordStatus.EMPTY -> it.password.prop.copy(
                            isErr = true,
                            errText = UiText.StringResource(R.string.empty_password)
                        )

                        PasswordStatus.TOO_SHORT -> it.password.prop.copy(
                            isErr = true,
                            errText = UiText.StringResource(R.string.password_too_short)
                        )

                        PasswordStatus.TOO_LONG -> it.password.prop.copy(
                            isErr = true,
                            errText = UiText.StringResource(R.string.password_too_long)
                        )

                        PasswordStatus.INVALID -> it.password.prop.copy(
                            isErr = true,
                            errText = UiText.StringResource(R.string.invalid_password)
                        )
                    }
                )
            )
        }

        return isValidEmail && isValidPassword
    }

    private fun startEmailValidationJob() = viewModelScope.launch {

    }

    private fun observeFailedAttemptCount() {
        viewModelScope.launch {
            logInAttempt.collectLatest {
                if (it > 3) _state.update { uiState ->
                    uiState.copy(isNewEmailUser = true)
                }.also { return@collectLatest }
            }
        }
    }
}