import ComposeApp
import GoogleSignIn
import SwiftUI
import UIKit

#if KYOKU_SWIFTUI
struct ContentView: View {
    var body: some View {
        RootView()
            .onOpenURL{ url in
                _ = GIDSignIn.sharedInstance.handle(url)
            }
    }
}
#else
struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController(
            googleAuthWrapper: GoogleAuthHelperImpl()
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        ComposeView()
            .ignoresSafeArea()
            .onOpenURL{ url in
                _ = GIDSignIn.sharedInstance.handle(url)
            }
    }
}
#endif
