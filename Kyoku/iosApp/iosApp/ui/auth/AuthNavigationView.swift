import SwiftUI

/// Auth graph host. Owns the navigation path so child screens stay free of navigation state and
/// only append `AuthRoute`s in response to viewmodel events.
struct AuthNavigationView: View {
    @State private var path: [AuthRoute] = []

    var body: some View {
        NavigationStack(path: $path) {
            SignInView(path: $path)
                .navigationDestination(for: AuthRoute.self) { route in
                    switch route {
                    case .signUp:
                        SignUpView()
                    case .forgotPassword(let email):
                        ForgotPasswordView(path: $path, email: email)
                    case .validateOtp:
                        EmptyView()
                    }
                }
        }
    }
}
