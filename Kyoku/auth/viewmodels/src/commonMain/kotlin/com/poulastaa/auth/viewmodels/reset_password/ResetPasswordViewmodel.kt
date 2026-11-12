package com.poulastaa.auth.viewmodels.reset_password

import com.poulastaa.auth.domain.AuthRepository
import com.poulastaa.auth.viewmodels.utils.normalizedPassword
import com.poulastaa.auth.viewmodels.utils.passwordError
import com.poulastaa.common.domain.result.ApiError
import com.poulastaa.common.domain.result.AppResult
import com.poulastaa.common.viewmodels.BaseViewmodel
import com.poulastaa.common.viewmodels.SnackbarType
import com.poulastaa.common.viewmodels.states.UiTextFiledState
import com.poulastaa.common.viewmodels.stringPasswordMismatchError
import com.poulastaa.common.viewmodels.stringResetPasswordSessionExpired
import com.poulastaa.common.viewmodels.stringResetPasswordSuccess
import com.poulastaa.common.domain.result.Error as NetworkError

class ResetPasswordViewmodel(
    private val repo: AuthRepository,
) : BaseViewmodel<ResetPasswordUiState, ResetPasswordUiAction, ResetPasswordUiEvent>(
    initialSate = ResetPasswordUiState(),
) {
    override suspend fun handleAction(action: ResetPasswordUiAction) {
        if (_uiState.value.isMakingApiCall) return

        when (action) {
            is ResetPasswordUiAction.OnTokenReceived -> updateState { copy(token = action.token) }

            // Editing either field clears the api error on both, since it was raised for the pair.
            is ResetPasswordUiAction.OnPasswordChange -> updateState {
                copy(
                    password = UiTextFiledState(value = action.password.normalizedPassword()),
                    confirmPassword = if (isPasswordApiError) UiTextFiledState(value = confirmPassword.value) else confirmPassword,
                    isPasswordApiError = false,
                )
            }

            is ResetPasswordUiAction.OnConfirmPasswordChange -> updateState {
                copy(
                    password = if (isPasswordApiError) UiTextFiledState(value = password.value) else password,
                    confirmPassword = UiTextFiledState(value = action.password.normalizedPassword()),
                    isPasswordApiError = false,
                )
            }

            ResetPasswordUiAction.OnPasswordVisibilityToggle -> updateState {
                copy(isPasswordVisible = isPasswordVisible.not())
            }

            ResetPasswordUiAction.OnConfirmPasswordVisibilityToggle -> updateState {
                copy(isConfirmPasswordVisible = isConfirmPasswordVisible.not())
            }

            ResetPasswordUiAction.ResetPassword -> resetPassword()
        }
    }

    private suspend fun resetPassword() {
        val state = _uiState.value
        val password = state.password.value
        val confirmPassword = state.confirmPassword.value

        val passwordError = password.passwordError()
        val confirmPasswordError = if (password != confirmPassword) stringPasswordMismatchError() else null

        updateState {
            copy(
                isMakingApiCall = passwordError == null && confirmPasswordError == null,
                password = UiTextFiledState(
                    value = password,
                    isError = passwordError != null,
                    errorMessage = passwordError,
                ),
                confirmPassword = UiTextFiledState(
                    value = confirmPassword,
                    isError = confirmPasswordError != null,
                    errorMessage = confirmPasswordError,
                ),
            )
        }

        if (passwordError != null || confirmPasswordError != null) return

        when (val result = repo.resetPassword(password, state.token)) {
            is AppResult.Error -> {
                updateState { copy(isMakingApiCall = false) }
                handleResetPasswordError(result.error.error)
            }

            is AppResult.Success -> {
                updateState { copy(isMakingApiCall = false) }
                showSnackbar(stringResetPasswordSuccess(), SnackbarType.Success)
                onEvent(ResetPasswordUiEvent.NavigateToSignIn)
            }
        }
    }

    private suspend fun handleResetPasswordError(error: NetworkError) {
        when (error) {
            // Token already used or invalid: the user has to request a new code.
            ApiError.Network.UNAUTHORIZED -> {
                showSnackbar(stringResetPasswordSessionExpired(), SnackbarType.Error)
                onEvent(ResetPasswordUiEvent.NavigateToForgotPassword)
            }

            ApiError.Authentication.SAME_PASSWORD -> setPasswordApiError(ApiError.Authentication.SAME_PASSWORD.message)
            ApiError.Authentication.INVALID_PASSWORD -> setPasswordApiError(ApiError.Authentication.INVALID_PASSWORD.message)

            ApiError.Authentication.USER_NOT_FOUND -> {
                showSnackbar(ApiError.Authentication.USER_NOT_FOUND.message, SnackbarType.Error)
                onEvent(ResetPasswordUiEvent.NavigateToForgotPassword)
            }

            else -> if (handleCommonError(error).not()) {
                showSnackbar(ApiError.Network.SOMETHING_WENT_WRONG.message, SnackbarType.Error)
            }
        }
    }

    // Message is shown in place of the screen subtext, so the fields only get the error outline.
    private fun setPasswordApiError(message: String) = updateState {
        copy(
            password = password.copy(isError = true, errorMessage = ""),
            confirmPassword = confirmPassword.copy(isError = true, errorMessage = ""),
            isPasswordApiError = true,
            passwordApiErrorMessage = message,
        )
    }
}
