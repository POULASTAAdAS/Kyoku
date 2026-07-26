import ComposeApp
import GoogleSignIn
import SwiftUI
import UIKit

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
