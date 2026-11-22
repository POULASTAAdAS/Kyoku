import SwiftUI

/// Auth graph host. Owns the auth `Router` so child screens stay free of navigation state and
/// only push `AuthRoute`s in response to viewmodel events.
struct AuthNavigationView: View {
    @State private var router = Router<AuthRoute>()

    var body: some View {
        NavigationStack(path: $router.path) {
            SignInView()
                .navigationDestination(for: AuthRoute.self) { route in
                    switch route {
                    case .signUp:
                        SignUpView()
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
