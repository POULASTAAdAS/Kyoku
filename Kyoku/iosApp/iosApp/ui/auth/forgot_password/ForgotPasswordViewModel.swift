import ComposeApp

/// Typed Swift API for the shared `ForgotPasswordViewmodel`. Kotlin action names live only here so
/// `ForgotPasswordView` gets autocomplete and compile-time checks instead of raw Kotlin class names.
typealias ForgotPasswordViewModel = ObservableViewModel<ForgotPasswordUiState, any ForgotPasswordUiAction, any ForgotPasswordUiEvent>

extension ObservableViewModel where State == ForgotPasswordUiState, Action == any ForgotPasswordUiAction, Event == any ForgotPasswordUiEvent {
    convenience init() {
        self.init { $0.forgotPasswordViewmodel() }
    }

    func emailChanged(_ email: String) { onAction(ForgotPasswordUiActionOnEmailChange(email: email)) }
    func getOtp(email: String) { onAction(ForgotPasswordUiActionGetOtp(email: email)) }
}
