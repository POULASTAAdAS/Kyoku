package com.poulastaa.auth.presentation.intro

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.auth.domain.AuthValidator
import com.poulastaa.auth.domain.intro.IntroRepository
import com.poulastaa.auth.domain.model.PasswordStatus
import com.poulastaa.auth.domain.model.DtoAuthResponseStatus
import com.poulastaa.auth.presentation.R
import com.poulastaa.auth.presentation.components.AuthAllowedNavigationScreen
import com.poulastaa.auth.presentation.intro.model.IntroAllowedNavigationScreens
import com.poulastaa.auth.presentation.intro.model.IntroUiState
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.utils.Email
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
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
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

    private var emailValidationJob: Job? = null
    private var failedAttempts = MutableStateFlow(0)

    override fun onCleared() {
        super.onCleared()
        emailValidationJob?.cancel()
    }

    fun onAction(action: IntroUiAction) {
        if ((_state.value.isGoogleAuthLoading || _state.value.isEmailAuthLoading) &&
            (action != IntroUiAction.ObPasswordVisibilityToggle &&
                    action != IntroUiAction.OnGoogleAuthCancel &&
                    action !is IntroUiAction.OnGoogleTokenReceive)
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
                if (validate().not()) {
                    _uiEvent.trySend(
                        IntroUiEvent.EmitToast(
                            UiText.StringResource(
                                R.string.check_spam_for_mail
                            )
                        )
                    )

                    return
                }

                _state.update {
                    it.copy(
                        isEmailAuthLoading = true
                    )
                }

                failedAttempts.update { it + 1 }

                viewModelScope.launch {
                    delay(2_000) // simulate network delay

                    when (val res = repo.emailLogIn(
                        _state.value.email.prop.value.trim(),
                        _state.value.password.prop.value.trim()
                    )) {
                        is Result.Error -> when (res.error) {
                            DataError.Network.NO_INTERNET -> _uiEvent.send(
                                IntroUiEvent.EmitToast(
                                    UiText.StringResource(CoreR.string.please_check_internet_connection)
                                )
                            )

                            else -> _uiEvent.send(
                                IntroUiEvent.EmitToast(
                                    UiText.StringResource(CoreR.string.something_went_wrong_try_again)
                                )
                            )
                        }

                        is Result.Success -> when (res.data) {
                            DtoAuthResponseStatus.USER_FOUND,
                            DtoAuthResponseStatus.USER_FOUND_NO_PLAYLIST,
                            DtoAuthResponseStatus.USER_FOUND_NO_ARTIST,
                            DtoAuthResponseStatus.USER_FOUND_NO_GENRE,
                            DtoAuthResponseStatus.USER_FOUND_NO_B_DATE,
                                -> {
                                _uiEvent.send(
                                    IntroUiEvent.EmitToast(
                                        UiText.StringResource(
                                            R.string.check_for_verificaiton_mail
                                        )
                                    )
                                )

                                emailValidationJob?.cancel()
                                emailValidationJob = startEmailValidationJob(
                                    email = _state.value.email.prop.value.trim(),
                                    navigationScreen = res.data
                                )

                                return@launch
                            }

                            DtoAuthResponseStatus.EMAIL_NOT_VALID -> _state.update {
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

                            DtoAuthResponseStatus.PASSWORD_DOES_NOT_MATCH -> _state.update {
                                it.copy(
                                    password = it.password.copy(
                                        prop = it.password.prop.copy(
                                            isErr = true,
                                            errText = UiText.StringResource(R.string.password_does_not_match)
                                        )
                                    )
                                )
                            }

                            DtoAuthResponseStatus.USER_NOT_FOUND -> _state.update {
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

            IntroUiAction.OnGoogleAuthClick -> _state.update {
                it.copy(
                    isGoogleAuthLoading = true
                )
            }

            IntroUiAction.OnGoogleAuthCancel -> _state.update {
                it.copy(
                    isGoogleAuthLoading = false
                )
            }

            is IntroUiAction.OnGoogleTokenReceive -> {
                val countryCode = action.context.resources.configuration.locales[0].country

                viewModelScope.launch {
                    when (val result = repo.googleOneTap(action.token, countryCode)) {
                        is Result.Error -> when (result.error) {
                            DataError.Network.NO_INTERNET -> _uiEvent.send(
                                IntroUiEvent.EmitToast(
                                    UiText.StringResource(CoreR.string.please_check_internet_connection)
                                )
                            )

                            else -> _uiEvent.send(
                                IntroUiEvent.EmitToast(
                                    UiText.StringResource(CoreR.string.something_went_wrong_try_again)
                                )
                            )
                        }

                        is Result.Success -> when (result.data) {
                            DtoAuthResponseStatus.USER_CREATED,
                            DtoAuthResponseStatus.USER_FOUND,
                            DtoAuthResponseStatus.USER_FOUND_NO_PLAYLIST,
                            DtoAuthResponseStatus.USER_FOUND_NO_ARTIST,
                            DtoAuthResponseStatus.USER_FOUND_NO_GENRE,
                            DtoAuthResponseStatus.USER_FOUND_NO_B_DATE,
                                -> {
                                val screen = when (result.data) {
                                    DtoAuthResponseStatus.USER_FOUND -> AuthAllowedNavigationScreen.HOME
                                    DtoAuthResponseStatus.USER_CREATED,
                                    DtoAuthResponseStatus.USER_FOUND_NO_PLAYLIST,
                                        -> AuthAllowedNavigationScreen.IMPORT_SPOTIFY_PLAYLIST

                                    DtoAuthResponseStatus.USER_FOUND_NO_ARTIST -> AuthAllowedNavigationScreen.PIC_ARTIST
                                    DtoAuthResponseStatus.USER_FOUND_NO_GENRE -> AuthAllowedNavigationScreen.PIC_GENRE
                                    DtoAuthResponseStatus.USER_FOUND_NO_B_DATE -> AuthAllowedNavigationScreen.SET_B_DATE
                                    else -> throw IllegalStateException("Invalid navigation screen")
                                }

                                if (screen == AuthAllowedNavigationScreen.HOME) _uiEvent.trySend(
                                    IntroUiEvent.EmitToast(
                                        UiText.StringResource(
                                            R.string.welcome_back
                                        )
                                    )
                                ) else _uiEvent.trySend(
                                    IntroUiEvent.EmitToast(
                                        UiText.StringResource(
                                            R.string.welcome
                                        )
                                    )
                                )

                                _uiEvent.send(
                                    IntroUiEvent.Navigate(
                                        IntroAllowedNavigationScreens.App(screen)
                                    )
                                )
                            }

                            else -> _uiEvent.send(
                                IntroUiEvent.EmitToast(
                                    UiText.StringResource(CoreR.string.something_went_wrong_try_again)
                                )
                            )
                        }
                    }
                }

                _state.update {
                    it.copy(
                        isGoogleAuthLoading = false
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

    private fun startEmailValidationJob(
        email: Email,
        navigationScreen: DtoAuthResponseStatus,
    ) = viewModelScope.launch {
        var maxTime = 7.minutes.inWholeSeconds

        // kick off delay
        delay(7.seconds.inWholeMilliseconds)

        while (maxTime > 0) {
            val res = repo.checkEmailVerificationStatus(email)
            when (res) {
                is Result.Error -> {
                    if (res.error == DataError.Network.NO_INTERNET) {
                        _uiEvent.send(
                            IntroUiEvent.EmitToast(
                                UiText.StringResource(
                                    CoreR.string.please_check_internet_connection
                                )
                            )
                        )
                    }

                    emailValidationJob?.cancel()
                    return@launch
                }

                is Result.Success -> if (res.data) {
                    _uiEvent.send(
                        IntroUiEvent.EmitToast(
                            UiText.StringResource(
                                R.string.welcome_back
                            )
                        )
                    )

                    val screen = when (navigationScreen) {
                        DtoAuthResponseStatus.USER_FOUND -> AuthAllowedNavigationScreen.HOME
                        DtoAuthResponseStatus.USER_FOUND_NO_PLAYLIST -> AuthAllowedNavigationScreen.IMPORT_SPOTIFY_PLAYLIST
                        DtoAuthResponseStatus.USER_FOUND_NO_ARTIST -> AuthAllowedNavigationScreen.PIC_ARTIST
                        DtoAuthResponseStatus.USER_FOUND_NO_GENRE -> AuthAllowedNavigationScreen.PIC_GENRE
                        DtoAuthResponseStatus.USER_FOUND_NO_B_DATE -> AuthAllowedNavigationScreen.SET_B_DATE
                        else -> throw IllegalStateException("Invalid navigation screen")
                    }

                    _uiEvent.send(
                        IntroUiEvent.Navigate(
                            IntroAllowedNavigationScreens.App(
                                screen
                            )
                        )
                    )

                    return@launch
                }
            }

            delay(5.seconds.inWholeMilliseconds)
            maxTime = maxTime.minus(5.seconds.inWholeSeconds)
        }
    }

    private fun observeFailedAttemptCount() {
        viewModelScope.launch {
            failedAttempts.collectLatest {
                if (it > 3) _state.update { uiState ->
                    uiState.copy(isNewEmailUser = true)
                }.also { return@collectLatest }
            }
        }
    }
}