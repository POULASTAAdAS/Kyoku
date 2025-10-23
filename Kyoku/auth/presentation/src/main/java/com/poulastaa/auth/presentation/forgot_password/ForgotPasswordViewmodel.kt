package com.poulastaa.auth.presentation.forgot_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.auth.domain.AuthValidator
import com.poulastaa.auth.domain.forgot_password.ForgotPasswordRepository
import com.poulastaa.auth.domain.model.DtoForgotPasswordSentStatus
import com.poulastaa.auth.presentation.R
import com.poulastaa.auth.presentation.forgot_password.ForgotPasswordUiEvent.EmitToast
import com.poulastaa.auth.presentation.forgot_password.ForgotPasswordUiEvent.OnNavigate
import com.poulastaa.auth.presentation.forgot_password.model.ForgotPasswordAllowedNavigationScreens
import com.poulastaa.auth.presentation.forgot_password.model.ForgotPasswordAllowedNavigationScreens.Verify
import com.poulastaa.auth.presentation.model.EmailTextProp
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.utils.Email
import com.poulastaa.core.presentation.designsystem.TextProp
import com.poulastaa.core.presentation.designsystem.UiText
import com.poulastaa.core.presentation.designsystem.UiText.StringResource
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
import java.util.Locale
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes
import com.poulastaa.core.presentation.ui.R as CoreR

private const val DEFAULT_TICKER = "01:00"

@HiltViewModel
class ForgotPasswordViewmodel @Inject constructor(
    private val validator: AuthValidator,
    private val repo: ForgotPasswordRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(ForgotPasswordUiState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(2.minutes.inWholeMilliseconds),
        initialValue = ForgotPasswordUiState(
            ticker = DEFAULT_TICKER
        )
    )

    private val _uiEvent = Channel<ForgotPasswordUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var email: Email? = null

    private var resetForgotPasswordButtonJob: Job? = null

    override fun onCleared() {
        super.onCleared()
        resetForgotPasswordButtonJob?.cancel()
    }

    fun populateEmailField(email: Email) {
        _state.update {
            it.copy(
                email = EmailTextProp(
                    isValidEmail = validator.isValidEmail(email),
                    prop = TextProp(email)
                )
            )
        }
    }

    fun onAction(action: ForgotPasswordUiAction) {
        if (_state.value.isTickerVisible &&
            action != ForgotPasswordUiAction.OnNavigateBack &&
            action != ForgotPasswordUiAction.OnVerifyClick
        ) return

        when (action) {
            is ForgotPasswordUiAction.OnEmailChange -> _state.update {
                it.copy(
                    email = EmailTextProp(
                        isValidEmail = validator.isValidEmail(action.email),
                        prop = TextProp(action.email)
                    )
                )
            }

            ForgotPasswordUiAction.OnNavigateBack -> {
                viewModelScope.launch {
                    _uiEvent.send(
                        OnNavigate(
                            ForgotPasswordAllowedNavigationScreens.NavigateBack
                        )
                    )
                }
            }

            ForgotPasswordUiAction.OnSummit -> {
                viewModelScope.launch {
                    if (validator.isValidEmail(_state.value.email.prop.value.trim()).not()) {
                        setInvalidEmail()
                        return@launch
                    }

                    _state.update { it.copy(isLoading = true) }
                    email = _state.value.email.prop.value.trim()

                    email?.let { it ->
                        when (val result = repo.askForForgotPasswordMail(it)) {
                            is Result.Error -> when (result.error) {
                                DataError.Network.NO_INTERNET -> _uiEvent.send(
                                    ForgotPasswordUiEvent.EmitToast(
                                        UiText.StringResource(CoreR.string.please_check_internet_connection)
                                    )
                                )

                                else -> _uiEvent.send(
                                    ForgotPasswordUiEvent.EmitToast(
                                        UiText.StringResource(
                                            CoreR.string.something_went_wrong_try_again
                                        )
                                    )
                                )
                            }

                            is Result.Success -> when (result.data) {
                                DtoForgotPasswordSentStatus.SENT -> _uiEvent.send(
                                    OnNavigate(
                                        Verify(it)
                                    )
                                ).also {
                                    resetForgotPasswordButtonJob?.cancel()
                                    resetForgotPasswordButtonJob = resetForgotPasswordButton()
                                }

                                DtoForgotPasswordSentStatus.INVALID_EMAIL -> setInvalidEmail()
                                DtoForgotPasswordSentStatus.ERROR -> _uiEvent.send(
                                    EmitToast(
                                        StringResource(
                                            CoreR.string.something_went_wrong_try_again
                                        )
                                    )
                                )

                                DtoForgotPasswordSentStatus.USER_NOT_FOUND -> _state.update {
                                    it.copy(
                                        email = it.email.copy(
                                            prop = it.email.prop.copy(
                                                isErr = true,
                                                errText = UiText.StringResource(CoreR.string.email_not_registered)
                                            )
                                        )
                                    )
                                }
                            }
                        }
                    }

                    _state.update { it.copy(isLoading = false) }
                }
            }

            ForgotPasswordUiAction.OnVerifyClick -> {
                if (_state.value.isVerifyButtonEnabled.not()) return

                viewModelScope.launch {
                    _uiEvent.send(OnNavigate(Verify(email)))
                }
            }
        }
    }

    private fun setInvalidEmail() {
        _state.update {
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
    }

    private fun resetForgotPasswordButton() = viewModelScope.launch {
        _state.update {
            it.copy(
                ticker = DEFAULT_TICKER,
                isTickerVisible = true,
                isVerifyButtonEnabled = true
            )
        }

        val totalDuration = 1.minutes.inWholeMilliseconds
        val start = System.currentTimeMillis()
        val endTime = start + totalDuration

        while (System.currentTimeMillis() < endTime) {
            delay(1_000)

            val remaining = endTime - System.currentTimeMillis()
            val totalSec = (remaining / 1000).coerceAtLeast(0)
            val min = totalSec / 60
            val sec = totalSec % 60

            _state.update {
                it.copy(
                    ticker = String.format(
                        Locale.ROOT,
                        format = "%02d:%02d",
                        min, sec
                    )
                )
            }
        }

        _state.update {
            it.copy(
                ticker = "00:00",
                isTickerVisible = false,
                isVerifyButtonEnabled = false
            )
        }
    }
}