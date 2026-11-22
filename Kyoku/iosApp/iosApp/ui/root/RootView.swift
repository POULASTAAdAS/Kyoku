import ComposeApp
import SwiftUI

struct RootView: View {
    @StateObject private var viewModel = RootViewModel()
    @StateObject private var commonUi = CommonUiStateObserver()
    @State private var flow: AppFlow?

    var body: some View {
        Group {
            if viewModel.state.isLoading || flow == nil {
                loadingView
            } else if let flow {
                switch flow {
                case .auth:
                    AuthNavigationView(
                        onNavigateToSetup: navigateToSetup,
                        onNavigateToMain: navigateToMain
                    )
                case .setup:
                    SetupNavigationView()
                case .main:
                    MainNavigationView()
                }
            }
        }
        .onAppear(perform: setInitialView)
        .onChange(of: viewModel.state.isLoading) { _, _ in
            setInitialView()
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

    private var loadingView: some View {
        ZStack {
            AppColors.background
                .ignoresSafeArea()

            ProgressView().tint(AppColors.primary)
        }
    }

    private func setInitialView() {
        guard !viewModel.state.isLoading, flow == nil else { return }

        switch viewModel.state.startDestination {
        case is ScreensSetupGraph:
            flow = .setup
        case is ScreensMainGraph:
            flow = .main
        default:
            flow = .auth
        }
    }

    private func navigateToSetup() {
        viewModel.destinationChanged(ScreensSetupScreensImportPlaylist.shared)
        flow = .setup
    }

    private func navigateToMain() {
        viewModel.destinationChanged(ScreensMainScreensHome.shared)
        flow = .main
    }
}
