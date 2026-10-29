import ComposeApp
import SwiftUI

struct SignInView: View {
    @StateObject private var viewModel = ObservableViewModel<SignInUiState, any SignInUiAction, any SignInUiEvent> {
        $0.signInViewmodel()
    }

    var body: some View {
        // Blank screen: the SwiftUI UI for this screen is not implemented yet.
        Color(.systemBackground)
            .ignoresSafeArea()
    }
}
