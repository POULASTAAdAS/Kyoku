package com.poulastaa.auth.presentation.singup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.auth.domain.AuthValidator
import com.poulastaa.auth.domain.email_signup.SingUpRepository
import com.poulastaa.auth.domain.model.DtoAuthResponseStatus
import com.poulastaa.auth.domain.model.PasswordStatus
import com.poulastaa.auth.domain.model.UsernameStatus
import com.poulastaa.auth.presentation.R
import com.poulastaa.auth.presentation.components.AuthAllowedNavigationScreen
import com.poulastaa.auth.presentation.intro.IntroUiEvent
import com.poulastaa.auth.presentation.model.EmailTextProp
import com.poulastaa.auth.presentation.intro.model.IntroAllowedNavigationScreens
import com.poulastaa.auth.presentation.model.PasswordTextProp
import com.poulastaa.auth.presentation.singup.model.EmailSingUpUiState
import com.poulastaa.auth.presentation.singup.model.UsernameTextProp
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.utils.Email
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
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import com.poulastaa.core.presentation.ui.R as CoreR

@HiltViewModel
internal class EmailSingUpViewmodel @Inject constructor(
    private val validator: AuthValidator,
    private val repo: SingUpRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(EmailSingUpUiState())
    val state = _state.onStart {
        observeFailedAttemptCount()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = EmailSingUpUiState()
    )

    private val _uiEvent = Channel<EmailSingUpUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var emailValidationJob: Job? = null
    private var failedAttempts = MutableStateFlow(0)

    override fun onCleared() {
        super.onCleared()
        emailValidationJob?.cancel()
    }

    fun populateEmail(email: Email) {
        _state.update {
            it.copy(
                email = EmailTextProp(
                    isValidEmail = validator.isValidEmail(email),
                    prop = TextProp(email)
                )
            )
        }
    }

    fun onAction(action: EmailSingUpUiAction) {
        if (_state.value.isLoading &&
            (action != EmailSingUpUiAction.OnPasswordVisibilityToggle ||
                    action != EmailSingUpUiAction.OnConformPasswordVisibilityToggle)
        ) {
            _uiEvent.trySend(
                EmailSingUpUiEvent.EmitToast(
                    UiText.StringResource(
                        R.string.check_spam_for_mail
                    )
                )
            )

            return
        }

        when (action) {
            is EmailSingUpUiAction.OnUsernameChange -> {
                val usernameValidationState = validator.isValidUserName(action.username)

                _state.update {
                    it.copy(
                        username = UsernameTextProp(
                            isValidUsername = usernameValidationState == UsernameStatus.VALID,
                            prop = TextProp(value = action.username)
                        )
                    )
                }
            }

            is EmailSingUpUiAction.OnEmailChange -> _state.update {
                it.copy(
                    email = EmailTextProp(
                        isValidEmail = validator.isValidEmail(action.email),
                        prop = TextProp(action.email)
                    )
                )
            }

            is EmailSingUpUiAction.OnPasswordChange -> _state.update {
                it.copy(
                    password = PasswordTextProp(
                        prop = TextProp(action.password)
                    )
                )
            }

            EmailSingUpUiAction.OnPasswordVisibilityToggle -> _state.update {
                it.copy(
                    password = it.password.copy(
                        isPasswordVisible = it.password.isPasswordVisible.not()
                    )
                )
            }

            is EmailSingUpUiAction.OnConformPasswordChange -> _state.update {
                it.copy(
                    conformPassword = PasswordTextProp(
                        prop = TextProp(action.password)
                    )
                )
            }

            EmailSingUpUiAction.OnConformPasswordVisibilityToggle -> _state.update {
                it.copy(
                    conformPassword = it.conformPassword.copy(
                        isPasswordVisible = it.conformPassword.isPasswordVisible.not()
                    )
                )
            }

            is EmailSingUpUiAction.OnSubmit -> {
                if (validate().not()) return

                _state.update {
                    it.copy(
                        isLoading = true
                    )
                }

                viewModelScope.launch {
                    val countryCode = action.context.resources.configuration.locales[0].country

                    when (val res = repo.emailSingUp(
                        email = _state.value.email.prop.value.trim(),
                        username = _state.value.username.prop.value.trim(),
                        password = _state.value.password.prop.value.trim(),
                        countryCode = countryCode
                    )) {
                        is Result.Error -> when (res.error) {
                            DataError.Network.NO_INTERNET -> _uiEvent.trySend(
                                EmailSingUpUiEvent.EmitToast(
                                    UiText.StringResource(
                                        CoreR.string.please_check_internet_connection
                                    )
                                )
                            )

                            else -> _uiEvent.trySend(
                                EmailSingUpUiEvent.EmitToast(
                                    UiText.StringResource(
                                        CoreR.string.something_went_wrong_try_again
                                    )
                                )
                            )
                        }

                        is Result.Success -> when (res.data) {
                            DtoAuthResponseStatus.USER_CREATED -> {
                                _uiEvent.send(
                                    EmailSingUpUiEvent.EmitToast(
                                        UiText.StringResource(
                                            R.string.check_for_verificaiton_mail
                                        )
                                    )
                                )

                                emailValidationJob?.cancel()
                                emailValidationJob = startEmailValidationJob(
                                    _state.value.email.prop.value.trim()
                                )

                                return@launch
                            }

                            DtoAuthResponseStatus.EMAIL_ALREADY_IN_USE,
                                -> {
                                _state.update {
                                    it.copy(
                                        email = it.email.copy(
                                            prop = it.email.prop.copy(
                                                isErr = true,
                                                errText = UiText.StringResource(
                                                    CoreR.string.email_already_registered
                                                )
                                            )
                                        )
                                    )
                                }

                                failedAttempts.update { it + 1 }
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

                            else -> _uiEvent.trySend(
                                EmailSingUpUiEvent.EmitToast(
                                    UiText.StringResource(
                                        CoreR.string.something_went_wrong_try_again
                                    )
                                )
                            )
                        }
                    }

                    _state.update { it.copy(isLoading = false) }
                }
            }

            EmailSingUpUiAction.OnEmailLogInClick -> viewModelScope.launch {
                _uiEvent.send(EmailSingUpUiEvent.OnNavigateBack)
            }
        }
    }

    private fun validate(): Boolean {
        var isValidUserName = false
        var isValidEmail = false
        var isValidPassword = false
        var isValidConformPassword = false

        val usernameState = validator.isValidUserName(_state.value.username.prop.value)
        when (usernameState) {
            UsernameStatus.VALID -> UiText.DynamicString("")
            UsernameStatus.INVALID -> UiText.StringResource(R.string.invalid_username)
            UsernameStatus.INVALID_START_WITH_UNDERSCORE -> UiText.StringResource(
                resId = R.string.invalid_username_start_with_underscore
            )

            UsernameStatus.EMPTY -> UiText.StringResource(R.string.invalid_username_empty)
            UsernameStatus.TOO_LONG -> UiText.StringResource(R.string.invalid_username_too_long)
        }.also { uiText ->
            if (usernameState == UsernameStatus.VALID) isValidUserName = true
            else _state.update {
                it.copy(
                    username = it.username.copy(
                        isValidUsername = false,
                        prop = it.username.prop.copy(
                            isErr = true,
                            errText = uiText
                        )
                    )
                )
            }
        }

        if (validator.isValidEmail(_state.value.email.prop.value)) isValidEmail = true
        else _state.update {
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

        val passwordState = validator.validatePassword(_state.value.password.prop.value)
        when (passwordState) {
            PasswordStatus.VALID -> UiText.DynamicString("")
            PasswordStatus.EMPTY -> UiText.StringResource(R.string.empty_password)
            PasswordStatus.TOO_SHORT -> UiText.StringResource(R.string.password_too_short)
            PasswordStatus.TOO_LONG -> UiText.StringResource(R.string.password_too_long)
            PasswordStatus.INVALID -> UiText.StringResource(R.string.invalid_password)
        }.also { uiText ->
            if (passwordState == PasswordStatus.VALID) isValidPassword = true
            else _state.update {
                it.copy(
                    password = it.password.copy(
                        prop = it.password.prop.copy(
                            isErr = true,
                            errText = uiText
                        )
                    )
                )
            }
        }

        if (_state.value.conformPassword.prop.value.isBlank()) _state.update {
            it.copy(
                conformPassword = it.conformPassword.copy(
                    prop = it.conformPassword.prop.copy(
                        isErr = true,
                        errText = UiText.StringResource(R.string.empty_password)
                    )
                )
            )
        } else if (_state.value.password.prop.value == _state.value.conformPassword.prop.value)
            isValidConformPassword = true
        else _state.update {
            it.copy(
                conformPassword = it.conformPassword.copy(
                    prop = it.conformPassword.prop.copy(
                        isErr = true,
                        errText = UiText.StringResource(R.string.password_does_not_match)
                    )
                )
            )
        }

        return isValidEmail && isValidPassword && isValidConformPassword && isValidUserName
    }

    private fun startEmailValidationJob(
        email: Email,
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
                            EmailSingUpUiEvent.EmitToast(
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
                        EmailSingUpUiEvent.EmitToast(
                            UiText.StringResource(
                                R.string.welcome_back
                            )
                        )
                    )

                    _uiEvent.send(EmailSingUpUiEvent.NavigateToSetUp)
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
                    uiState.copy(isOldUser = true)
                }.also { return@collectLatest }
            }
        }
    }
}