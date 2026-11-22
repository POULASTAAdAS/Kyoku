import SwiftUI

/// Main graph host. Main destinations will use this stack as their SwiftUI screens are implemented.
struct MainNavigationView: View {
    @State private var router = Router<MainRoute>()

    var body: some View {
        NavigationStack(path: $router.path) {
            EmptyView()
        }
        .environment(router)
    }
}
