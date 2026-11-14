import ComposeApp
import SwiftUI

struct RootView: View {
    @StateObject private var viewModel = ObservableViewModel<RootUiState, any RootUiAction, KotlinNothing> {
        $0.rootViewmodel()
    }

    var body: some View {
        if viewModel.state.isLoading {
            ZStack {
                Color(.systemBackground)
                    .ignoresSafeArea()

                ProgressView()
            }
        } else if viewModel.state.startDestination is ScreensAuthGraph {
            SignInView()
        } else {
            // Setup and main graphs are not implemented in SwiftUI yet.
            EmptyView()
        }
    }
}
