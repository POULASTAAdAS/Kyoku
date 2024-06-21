package com.poulastaa.auth.presentation.email.forgot_password

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.auth.domain.Validator
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
class ForgotPasswordViewModel @Inject constructor(
    private val validator: Validator,
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
                    isValidEmail = validator.isValidEmail(state.email.data),
                    email = state.email.copy(
                        data = event.email,
                        isErr = false,
                        errText = UiText.DynamicString("")
                    )
                )
            }

            ForgotPasswordUiEvent.OnSendClick -> {
                if (state.isMakingApiCall) return

                if (!validator.isValidEmail(state.email.data)) {
                    state = state.copy(
                        email = state.email.copy(
                            isErr = true,
                            errText = UiText.StringResource(R.string.error_invalid_email)
                        )
                    )

                    return
                }

                state = state.copy(
                    isMakingApiCall = true
                )
            }
        }
    }
}