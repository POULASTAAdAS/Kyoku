import SwiftUI

/// Setup graph host. Its router is independent from auth so changing graphs discards auth history.
struct SetupNavigationView: View {
    @State private var router = Router<SetupRoute>()

    var body: some View {
        NavigationStack(path: $router.path) {
            ImportPlaylistView()
        }
        .environment(router)
    }
}
