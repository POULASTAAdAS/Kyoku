import ComposeApp

/// Typed Swift API for the shared `SignUpViewmodel`. Kotlin action names live only here so
/// `SignUpView` gets autocomplete and compile-time checks instead of raw Kotlin class names.
typealias SignUpViewModel = ObservableViewModel<SingUpUiState, any SignUpUiAction, any SignUpUiEvent>

extension ObservableViewModel where State == SingUpUiState, Action == any SignUpUiAction, Event == any SignUpUiEvent {
    convenience init() {
        self.init { $0.signUpViewmodel() }
    }

    func emailChanged(_ email: String) { onAction(SignUpUiActionOnEmailChange(email: email)) }
    func passwordChanged(_ password: String) { onAction(SignUpUiActionOnPasswordChange(password: password)) }
    func usernameChanged(_ username: String) { onAction(SignUpUiActionOnUsernameChange(username: username)) }
    func togglePasswordVisibility() { onAction(SignUpUiActionOnPasswordVisibilityToggle.shared) }
    func loginClicked() { onAction(SignUpUiActionOnLoginClick.shared) }
    func googleSignInClicked() { onAction(SignUpUiActionOnGoogleSignInClick.shared) }
    func signUp() { onAction(SignUpUiActionSignUp.shared) }

    func googleAuthFinished(_ result: GoogleAuthResult) {
        switch onEnum(of: result) {
        case .success(let success): onAction(SignUpUiActionOnGoogleTokenReceived(token: success.token))
        case .canceled, .error: onAction(SignUpUiActionOnGoogleAuthCanceled.shared)
        }
    }
}
