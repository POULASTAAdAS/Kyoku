import Foundation

/// All navigation routes live here, one enum per graph (mirrors Kotlin `Screens`).
/// Every route enum conforms to `Route`, the only type `Router` accepts.
protocol Route: Hashable {}

/// Destinations reachable from the auth graph; mirrors `Screens.AuthScreens` on the Kotlin side.
/// `SignIn` is the stack root, so it has no case here.
enum AuthRoute: Route {
    case signUp
    case forgotPassword(email: String?)
    case validateOtp(email: String)
}
