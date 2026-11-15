import ComposeApp
import SwiftUI

struct ForgotPasswordView: View {
    @StateObject private var viewModel = ForgotPasswordViewModel()

    var body: some View {
        // Blank screen: the SwiftUI UI for this screen is not implemented yet.
        AppColors.background
            .ignoresSafeArea()
    }
}
