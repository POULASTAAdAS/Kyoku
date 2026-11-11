package com.poulastaa.auth.viewmodels.otp

sealed interface OtpUiAction {
    data class OnEmailReceived(val email: String) : OtpUiAction
    data class OnOtpChange(val otp: String) : OtpUiAction
    data object ValidateOtp : OtpUiAction
}
