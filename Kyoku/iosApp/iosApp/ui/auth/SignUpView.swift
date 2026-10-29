import ComposeApp
import SwiftUI

struct SignUpView: View {
    @StateObject private var viewModel = ObservableViewModel<SingUpUiState, any SignUpUiAction, any SignUpUiEvent> {
        $0.signUpViewmodel()
    }

    var body: some View {
        // Blank screen: the SwiftUI UI for this screen is not implemented yet.
        Color(.systemBackground)
            .ignoresSafeArea()
    }
}
