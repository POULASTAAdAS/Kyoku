import Foundation

/// All navigation routes live here, one enum per graph (mirrors Kotlin `Screens`).
/// Every route enum conforms to `Route`, the only type `Router` accepts.
protocol Route: Hashable {}

/// The graph currently owning the app's UI. Switching flow replaces the whole navigation stack.
enum AppFlow {
    case auth
    case setup
    case main
}

/// Destinations reachable from the auth graph; mirrors `Screens.AuthScreens` on the Kotlin side.
/// `SignIn` is the stack root, so it has no case here.
enum AuthRoute: Route {
    case signUp
    case forgotPassword(email: String?)
    case validateOtp(email: String)
    case newPassword(email: String, token: String)
}

/// Setup has its own stack; destinations are added as their SwiftUI screens are implemented.
enum SetupRoute: Route {}

/// Main has its own stack; destinations are added as their SwiftUI screens are implemented.
enum MainRoute: Route {}
