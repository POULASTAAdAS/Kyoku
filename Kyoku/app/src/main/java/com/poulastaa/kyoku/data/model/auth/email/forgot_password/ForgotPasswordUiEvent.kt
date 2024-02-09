package com.poulastaa.kyoku.data.model.auth.email.forgot_password

sealed class ForgotPasswordUiEvent {
    data class OnEmailEnter(val data: String) : ForgotPasswordUiEvent()
    data class OnAutoFillEmailClick(val email: String) : ForgotPasswordUiEvent()

    data class EmitEmailSupportingText(val message: String) : ForgotPasswordUiEvent()

    data object OnGetEmailClick : ForgotPasswordUiEvent()
    data object OnNavigateBackClicked : ForgotPasswordUiEvent()

    data object OnCancel: ForgotPasswordUiEvent()

    data class EmitToast(val message: String) : ForgotPasswordUiEvent()
    data object SomeErrorOccurred : ForgotPasswordUiEvent()
}