package com.poulastaa.auth.viewmodels.otp

import com.poulastaa.auth.domain.AuthRepository
import com.poulastaa.auth.domain.model.DtoOtpValidationStatus
import com.poulastaa.auth.viewmodels.utils.OTP_LENGTH
import com.poulastaa.auth.viewmodels.utils.normalizedEmail
import com.poulastaa.common.domain.result.ApiError
import com.poulastaa.common.domain.result.AppResult
import com.poulastaa.common.viewmodels.BaseViewmodel
import com.poulastaa.common.viewmodels.SnackbarAction
import com.poulastaa.common.viewmodels.SnackbarType
import com.poulastaa.common.viewmodels.states.UiTextFiledState
import com.poulastaa.common.viewmodels.stringOk
import com.poulastaa.common.viewmodels.stringOtpExpiredError
import com.poulastaa.common.viewmodels.stringOtpInvalidError
import com.poulastaa.common.viewmodels.stringOtpLeaveMessage
import com.poulastaa.common.viewmodels.stringOtpLengthError
import com.poulastaa.common.viewmodels.stringOtpVerificationSuccess
import com.poulastaa.common.domain.result.Error as NetworkError

class OtpViewmodel(
    private val repo: AuthRepository,
) : BaseViewmodel<OtpUiState, OtpUiAction, OtpUiEvent>(
    initialSate = OtpUiState(),
) {
    override suspend fun handleAction(action: OtpUiAction) {
        if (_uiState.value.isMakingApiCall) return

        when (action) {
            is OtpUiAction.OnEmailReceived -> updateState { copy(email = action.email.normalizedEmail()) }

            is OtpUiAction.OnOtpChange -> {
                val otp = action.otp.filter { it.isDigit() }.take(OTP_LENGTH)
                updateState { copy(otp = UiTextFiledState(value = otp)) }

                if (otp.length == OTP_LENGTH) validateOtp()
            }

            OtpUiAction.ValidateOtp -> validateOtp()

            OtpUiAction.OnBackClick -> showSnackbar(
                message = stringOtpLeaveMessage(),
                type = SnackbarType.Info,
                action = SnackbarAction(label = stringOk()) {
                    onEvent(OtpUiEvent.NavigateBack)
                },
            )
        }
    }

    private suspend fun validateOtp() {
        val state = _uiState.value
        val otp = state.otp.value

        if (otp.length != OTP_LENGTH) return setOtpError(stringOtpLengthError(OTP_LENGTH))

        updateState { copy(isMakingApiCall = true, otp = UiTextFiledState(value = otp)) }

        when (val result = repo.validateForgotPasswordOtp(state.email, otp)) {
            is AppResult.Error -> {
                updateState { copy(isMakingApiCall = false) }
                handleValidationError(result.error.error)
            }

            is AppResult.Success -> {
                updateState { copy(isMakingApiCall = false) }

                when (result.response.status) {
                    DtoOtpValidationStatus.VALID -> {
                        showSnackbar(stringOtpVerificationSuccess(), SnackbarType.Success)
                        onEvent(OtpUiEvent.NavigateToResetPassword(result.response.token, state.email))
                    }
                    DtoOtpValidationStatus.INVALID_CODE -> setOtpError(stringOtpInvalidError())
                    DtoOtpValidationStatus.EXPIRED -> setOtpError(stringOtpExpiredError())
                    DtoOtpValidationStatus.USER_NOT_FOUND -> setOtpError(ApiError.Authentication.USER_NOT_FOUND.message)
                    DtoOtpValidationStatus.INVALID_EMAIL -> setOtpError(ApiError.Authentication.EMAIL_NOT_VALID.message)
                }
            }
        }
    }

    private suspend fun handleValidationError(error: NetworkError) {
        if (handleCommonError(error)) return

        when (error) {
            ApiError.Authentication.INVALID_OTP -> setOtpError(stringOtpInvalidError())
            ApiError.Authentication.OTP_EXPIRED -> setOtpError(stringOtpExpiredError())
            ApiError.Authentication.USER_NOT_FOUND -> setOtpError(ApiError.Authentication.USER_NOT_FOUND.message)
            ApiError.Authentication.EMAIL_NOT_VALID -> setOtpError(ApiError.Authentication.EMAIL_NOT_VALID.message)

            else -> setOtpError(ApiError.Network.SOMETHING_WENT_WRONG.message)
        }
    }

    private fun setOtpError(message: String) {
        updateState { copy(otp = otp.copy(isError = true)) }
        showSnackbar(message, SnackbarType.Error)
    }
}
