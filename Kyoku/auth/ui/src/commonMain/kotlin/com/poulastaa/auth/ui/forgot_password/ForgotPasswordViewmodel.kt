package com.poulastaa.auth.ui.forgot_password

import androidx.compose.runtime.Immutable
import com.poulastaa.auth.ui.utils.emailError
import com.poulastaa.auth.ui.utils.normalizedEmail
import com.poulastaa.common.ui.states.UiTextFiledState
import com.poulastaa.common.ui.viewmodel.BaseViewmodel

@Immutable
class ForgotPasswordViewmodel :
    BaseViewmodel<ForgotPasswordUiState, ForgotPasswordUiAction, ForgotPasswordUiEvent>(
        initialSate = ForgotPasswordUiState(),
    ) {
    override suspend fun handleAction(action: ForgotPasswordUiAction) {
        if (_uiState.value.isMakingApiCall) return

        when (action) {
            is ForgotPasswordUiAction.OnEmailChange -> {
                val email = action.email.normalizedEmail()
                val emailError = email.emailError()

                updateState {
                    copy(
                        email = UiTextFiledState(
                            value = email,
                            isError = emailError != null,
                            errorMessage = emailError,
                        ),
                    )
                }
            }

            is ForgotPasswordUiAction.GetOtp -> {
                val email = action.email.normalizedEmail()
                val emailError = email.emailError()

                if (emailError != null) {
                    updateState {
                        copy(
                            email = UiTextFiledState(
                                value = email,
                                isError = true,
                                errorMessage = emailError,
                            ),
                        )
                    }

                    return
                }

                updateState {
                    copy(
                        isMakingApiCall = true,
                        email = UiTextFiledState(
                            value = email,
                            isError = false,
                            errorMessage = emailError,
                        ),
                    )
                }

                //TODO: make api call

                updateState { copy(isMakingApiCall = false) }
                onEvent(ForgotPasswordUiEvent.NavigateToOtp(email))
            }
        }
    }
}
