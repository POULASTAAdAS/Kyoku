import ComposeApp
import Foundation

/// Mirrors the shared `CommonUiStateHolder` (app-wide messages a viewmodel sends with
/// `showSnackbar`) into SwiftUI. Created once in `RootView` and handed down as an environment
/// object, so the root can show errors as an alert and screens can show their own status inline.
final class CommonUiStateObserver: ObservableObject {
    @Published private(set) var message: SnackbarData?

    private var handle: (any Kotlinx_coroutines_coreDisposableHandle)?

    /// Error messages; the root presents these as a system alert.
    var error: SnackbarData? { message?.type == .error ? message : nil }

    /// Success / info messages; screens present these inline, next to what triggered them.
    var status: SnackbarData? { message?.type == .error ? nil : message }

    init() {
        let holder = CommonUiStateHolder.shared
        message = holder.uiState.value?.snackbarData

        // FlowWrapper.subscribe collects on Dispatchers.Main, so @Published can be set directly.
        handle = holder.uiState.subscribe(onCollect: { [weak self] state in
            self?.message = state?.snackbarData
        })
    }

    func dismiss() {
        CommonUiStateHolder.shared.dismissSnackbar()
    }

    deinit {
        handle?.dispose()
    }
}
