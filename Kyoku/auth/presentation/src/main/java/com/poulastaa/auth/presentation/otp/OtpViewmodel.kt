package com.poulastaa.auth.presentation.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.presentation.Email
import com.poulastaa.core.presentation.designsystem.TextProp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes

@HiltViewModel
internal class OtpViewmodel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(OtpUiState())
    val uiState = _state.onStart {
        tickerJob?.cancel()
        tickerJob = startTicker()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(2_000),
        initialValue = OtpUiState()
    )

    private val _uiEvent = Channel<OtpUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var tickerJob: Job? = null
    private var failedAttempt: Int = 0

    override fun onCleared() {
        super.onCleared()
        tickerJob?.cancel()
    }

    fun loadEmail(email: Email) {
        _state.update {
            it.copy(email = email)
        }
    }

    fun onAction(action: OtpUiAction) {
        if (_state.value.isLoading && (
                    action == OtpUiAction.OnSubmit ||
                            action is OtpUiAction.OnOTPChange ||
                            action == OtpUiAction.OnResendOTP
                    )
        ) return

        when (action) {
            is OtpUiAction.OnOTPChange -> {
                action.otp.forEach { char ->
                    if (char.isDigit().not()) return
                }

                if (action.otp.length >= 6) return

                _state.update {
                    it.copy(
                        otp = TextProp(action.otp)
                    )
                }
            }

            OtpUiAction.OnResendOTP -> onAction(OtpUiAction.OnSubmit).also {
                tickerJob?.cancel()
                tickerJob = startTicker()
            }

            OtpUiAction.OnSubmit -> {
                failedAttempt++;

                _state.update {
                    it.copy(isLoading = true)
                }
            }

            OtpUiAction.OnStateNavigateBackFlow -> _state.update { it.copy(returnState = ReturnState.WARNING) }

            OtpUiAction.OnDirectBack -> viewModelScope.launch {
                _uiEvent.send(OtpUiEvent.NavigateBack)
            }

            OtpUiAction.OnBackConform -> {
                if (_state.value.returnState == ReturnState.WARNING) _state.update {
                    it.copy(
                        returnState = ReturnState.CONFIRM
                    )
                } else onAction(OtpUiAction.OnDirectBack)
            }

            OtpUiAction.OnBackCancel -> _state.update { it.copy(returnState = ReturnState.IDEAL) }
        }
    }

    private fun startTicker() = viewModelScope.launch {
        _state.update {
            it.copy(
                resendState = ResendState.TICKER,
                ticker = "04:00"
            )
        }

        val durationMillis = 4.minutes.inWholeMilliseconds
        val start = System.currentTimeMillis()
        val endTime = start + durationMillis

        val twoPointFiveMinute = 2.minutes.inWholeSeconds

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

            if (_state.value.isTryAnotherEmailVisible.not() &&
                totalSec < twoPointFiveMinute && failedAttempt > 1
            ) _state.update { it.copy(isTryAnotherEmailVisible = true) }
        }

        _state.update {
            it.copy(
                ticker = "00:00",
                resendState = ResendState.ENABLED,
                isLoading = false
            )
        }
    }
}