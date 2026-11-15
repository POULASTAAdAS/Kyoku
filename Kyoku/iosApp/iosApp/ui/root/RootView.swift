import ComposeApp
import SwiftUI

struct RootView: View {
    @StateObject private var viewModel = ObservableViewModel<RootUiState, any RootUiAction, KotlinNothing> {
        $0.rootViewmodel()
    }
    @StateObject private var commonUi = CommonUiStateObserver()

    var body: some View {
        Group {
            if viewModel.state.isLoading {
                ZStack {
                    AppColors.background
                        .ignoresSafeArea()

                    ProgressView().tint(AppColors.primary)
                }
            } else if viewModel.state.startDestination is ScreensAuthGraph {
                AuthNavigationView()
            } else {
                // Setup and main graphs are not implemented in SwiftUI yet.
                EmptyView()
            }
        }
        .environmentObject(commonUi)
        // Errors that need acknowledging (offline, server down) use the system alert on iOS,
        // not a self-dismissing toast. Field-level errors stay inline in each screen's form.
        .alert(
            commonUi.error?.message ?? "",
            isPresented: Binding(
                get: { commonUi.error != nil },
                set: { if !$0 { commonUi.dismiss() } }
            )
        ) {
            Button("OK", role: .cancel) {}
        }
    }
}
