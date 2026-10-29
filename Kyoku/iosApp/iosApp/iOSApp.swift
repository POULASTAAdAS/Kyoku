import ComposeApp
import SwiftUI

@main
struct iOSApp: App {
    init() {
#if KYOKU_SWIFTUI
        // In Compose mode MainViewController's configure block does this instead.
        IosViewModelProviderKt.doInitKoinIos()
        IosViewModelProviderKt.registerGoogleAuth(wrapper: GoogleAuthHelperImpl())
#endif
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
