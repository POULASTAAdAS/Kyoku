import ComposeApp
import SwiftUI

struct ForgotPasswordView: View {
    @StateObject private var viewModel = ObservableViewModel<ForgotPasswordUiState, any ForgotPasswordUiAction, any ForgotPasswordUiEvent> {
        $0.forgotPasswordViewmodel()
    }

    var body: some View {
        // Blank screen: the SwiftUI UI for this screen is not implemented yet.
        Color(.systemBackground)
            .ignoresSafeArea()
    }
}
