import ComposeApp

/// Typed Swift API for the shared `OtpViewmodel`. Kotlin action names live only here so
/// `OtpView` gets autocomplete and compile-time checks instead of raw Kotlin class names.
typealias OtpViewModel = ObservableViewModel<
    OtpUiState, any OtpUiAction, any OtpUiEvent
>

extension ObservableViewModel
where State == OtpUiState, Action == any OtpUiAction, Event == any OtpUiEvent {
    convenience init() {
        self.init { $0.otpViewmodel() }
    }

    func emailReceived(_ email: String) {
        onAction(OtpUiActionOnEmailReceived(email: email))
    }
    func otpChanged(_ otp: String) {
        onAction(OtpUiActionOnOtpChange(otp: otp))
    }
    func validateOtp() { onAction(OtpUiActionValidateOtp.shared) }
    func backClicked() { onAction(OtpUiActionOnBackClick.shared) }
}
