package com.poulastaa.auth.viewmodels.forgot_password

import com.poulastaa.auth.domain.AuthRepository
import com.poulastaa.auth.domain.model.DtoForgotPasswordStatus
import com.poulastaa.auth.viewmodels.utils.emailError
import com.poulastaa.auth.viewmodels.utils.normalizedEmail
import com.poulastaa.common.network.ApiError
import com.poulastaa.common.network.AppResult
import com.poulastaa.common.viewmodels.BaseViewmodel
import com.poulastaa.common.viewmodels.states.UiTextFiledState

class ForgotPasswordViewmodel(
    private val repo: AuthRepository,
) : BaseViewmodel<ForgotPasswordUiState, ForgotPasswordUiAction, ForgotPasswordUiEvent>(
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
                            errorMessage = emailError ?: "",
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
                            errorMessage = emailError ?: "",
                        ),
                    )
                }

                when (val result = repo.sendForgotPasswordMail(email)) {
                    is AppResult.Error -> {
                        updateState { copy(isMakingApiCall = false) }
                        if (handleCommonError(result.error.error)) return
                    }

                    is AppResult.Success -> {
                        when (result.response.status) {
                            DtoForgotPasswordStatus.SENT -> onEvent(
                                ForgotPasswordUiEvent.NavigateToOtp(
                                    email
                                )
                            )

                            DtoForgotPasswordStatus.USER_NOT_FOUND -> setEmailError(ApiError.Authentication.USER_NOT_FOUND.message)
                            DtoForgotPasswordStatus.INVALID_EMAIL -> setEmailError(ApiError.Authentication.EMAIL_NOT_VALID.message)
                            DtoForgotPasswordStatus.ERROR -> setEmailError(ApiError.Network.SOMETHING_WENT_WRONG.message)
                        }

                        updateState { copy(isMakingApiCall = false) }
                    }
                }
            }
        }
    }

    private fun setEmailError(message: String) = updateState {
        copy(email = email.copy(isError = true, errorMessage = message))
    }
}
