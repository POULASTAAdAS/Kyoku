package com.poulastaa.auth.presentation.otp

internal sealed interface OtpUiAction {
    data object OnStateNavigateBackFlow : OtpUiAction
    data object OnDirectBack : OtpUiAction

    data object OnBackConform : OtpUiAction
    data object OnBackCancel : OtpUiAction

    data class OnOTPChange(val otp: String) : OtpUiAction
    data object OnSubmit : OtpUiAction
    data object OnResendOTP : OtpUiAction
}