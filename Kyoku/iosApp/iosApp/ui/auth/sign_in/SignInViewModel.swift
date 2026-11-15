import ComposeApp

/// Typed Swift API for the shared `SignInViewmodel`. Kotlin action names live only here so
/// `SignInView` gets autocomplete and compile-time checks instead of raw Kotlin class names.
typealias SignInViewModel = ObservableViewModel<SignInUiState, any SignInUiAction, any SignInUiEvent>

extension ObservableViewModel where State == SignInUiState, Action == any SignInUiAction, Event == any SignInUiEvent {
    convenience init() {
        self.init { $0.signInViewmodel() }
    }

    func emailChanged(_ email: String) { onAction(SignInUiActionOnEmailChange(email: email)) }
    func passwordChanged(_ password: String) { onAction(SignInUiActionOnPasswordChange(password: password)) }
    func togglePasswordVisibility() { onAction(SignInUiActionOnPasswordVisibilityToggle.shared) }
    func credentialFieldFocused() { onAction(SignInUiActionOnCredentialFieldFocus.shared) }
    func forgotPasswordClicked(email: String) { onAction(SignInUiActionOnForgotPasswordClick(email: email)) }
    func createAccountClicked() { onAction(SignInUiActionOnCreateAccountClick.shared) }
    func googleSignInClicked() { onAction(SignInUiActionOnGoogleSignInClick.shared) }
    func signIn() { onAction(SignInUiActionSignIn.shared) }

    func googleAuthFinished(_ result: GoogleAuthResult) {
        switch onEnum(of: result) {
        case .success(let success): onAction(SignInUiActionOnGoogleAuthSuccess(token: success.token))
        case .canceled, .error: onAction(SignInUiActionOnGoogleAuthCanceled.shared)
        }
    }
}
