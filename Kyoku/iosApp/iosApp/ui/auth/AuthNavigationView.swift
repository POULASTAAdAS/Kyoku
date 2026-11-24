import SwiftUI

/// Auth graph host. Owns the auth `Router` so child screens stay free of navigation state and
/// only push `AuthRoute`s in response to viewmodel events.
struct AuthNavigationView: View {
    let onNavigateToSetup: () -> Void
    let onNavigateToMain: () -> Void

    @State private var router = Router<AuthRoute>()

    var body: some View {
        NavigationStack(path: $router.path) {
            SignInView(
                onNavigateToSetup: onNavigateToSetup,
                onNavigateToMain: onNavigateToMain
            )
            .navigationDestination(for: AuthRoute.self) { route in
                switch route {
                case .signUp:
                    SignUpView(
                        onNavigateToImportPlaylist: onNavigateToSetup,
                        onNavigateToMain: onNavigateToMain
                    )
                case .forgotPassword(let email):
                    ForgotPasswordView(email: email)
                case .validateOtp(let email):
                    OtpView(email: email)
                case .newPassword(
                    let email,
                    let token
                ):
                    NewPasswordView(
                        email: email,
                        token: token
                    )
                }
            }
        }
        .environment(router)
    }
}
