import Foundation

/// Destinations reachable from the auth graph; mirrors `Screens.AuthScreens` on the Kotlin side.
/// `SignIn` is the stack root, so it has no case here.
enum AuthRoute: Hashable {
    case signUp
    case forgotPassword(email: String?)
    case validateOtp(email: String)
}
