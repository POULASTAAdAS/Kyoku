package com.poulastaa.auth.presentation.email.forgot_password

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.auth.domain.Validator
import com.poulastaa.core.domain.auth.AuthRepository
import com.poulastaa.core.domain.model.ForgotPasswordSetStatus
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
class ForgotPasswordViewModel @Inject constructor(
    private val validator: Validator,
    private val auth: AuthRepository,
) : ViewModel() {
    var state by mutableStateOf(ForgotPasswordUiState())
        private set

    private val _uiEvent = Channel<ForgotPasswordUiAction>()
    val uiAction = _uiEvent.receiveAsFlow()

    private var resendVerificationMailJob: Job? = null

    fun onEvent(event: ForgotPasswordUiEvent) {
        when (event) {
            ForgotPasswordUiEvent.OnBackClick -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiEvent.send(ForgotPasswordUiAction.NavigateBack)
                }

                resendVerificationMailJob?.cancel()
            }

            is ForgotPasswordUiEvent.OnEmailChange -> {
                state = state.copy(
                    isValidEmail = validator.isValidEmail(event.email),
                    canResendMail = validator.isValidEmail(event.email),
                    email = state.email.copy(
                        data = event.email,
                        isErr = false,
                        errText = UiText.DynamicString("")
                    )
                )
            }

            ForgotPasswordUiEvent.OnSendClick -> {
                if (state.isMakingApiCall) return

                state = state.copy(
                    isMakingApiCall = true
                )

                viewModelScope.launch(Dispatchers.IO) {
                    when (val response = auth.sendForgotPasswordMail(state.email.data)) {
                        is Result.Error -> {
                            if (response.error == DataError.Network.NO_INTERNET) {
                                _uiEvent.send(
                                    ForgotPasswordUiAction.EmitToast(
                                        UiText.StringResource(R.string.error_no_internet)
                                    )
                                )
                            } else {
                                _uiEvent.send(
                                    ForgotPasswordUiAction.EmitToast(
                                        UiText.StringResource(R.string.error_something_went_wrong)
                                    )
                                )
                            }
                        }

                        is Result.Success -> {
                            when (response.data) {
                                ForgotPasswordSetStatus.SENT -> {
                                    state = state.copy(
                                        isResendVerificationMailSent = true
                                    )
                                }

                                ForgotPasswordSetStatus.NO_USER_FOUND -> {
                                    state = state.copy(
                                        email = state.email.copy(
                                            isErr = true,
                                            errText = UiText.StringResource(R.string.error_user_not_found)
                                        )
                                    )
                                }
                            }
                        }
                    }

                    state = state.copy(
                        isMakingApiCall = false,
                        canResendMail = false,
                        resendMailCounter = "40 s"
                    )

                    resendVerificationMailJob?.cancel()
                    resendVerificationMailJob = resendVerificationMailCounter()
                }
            }
        }
    }

    private fun resendVerificationMailCounter() = viewModelScope.launch(Dispatchers.IO) {
        for (i in 39 downTo 1) {
            delay(1000L)

            state = state.copy(
                resendMailCounter = "$i s"
            )
        }

        state = state.copy(
            isMakingApiCall = false,
            canResendMail = true,
            resendMailCounter = "Send Again"
        )
    }
}