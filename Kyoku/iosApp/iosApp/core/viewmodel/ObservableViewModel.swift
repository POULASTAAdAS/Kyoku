import ComposeApp
import Foundation

/// Bridges a shared Kotlin `BaseViewmodel` to SwiftUI.
///
/// Owns its own `IosViewModelProvider` (one `ViewModelStore` per screen), mirrors `uiState`
/// into `state`, forwards actions and events, and clears the viewmodel when deallocated.
final class ObservableViewModel<State: AnyObject, Action: AnyObject, Event: AnyObject>: ObservableObject {
    @Published private(set) var state: State

    private let provider: IosViewModelProvider
    private let viewModel: BaseViewmodel<State, Action, Event>
    private var stateHandle: (any Kotlinx_coroutines_coreDisposableHandle)?
    private var eventHandle: (any Kotlinx_coroutines_coreDisposableHandle)?

    init(_ make: (IosViewModelProvider) -> BaseViewmodel<State, Action, Event>) {
        let provider = IosViewModelProvider()
        let viewModel = make(provider)

        self.provider = provider
        self.viewModel = viewModel
        self.state = viewModel.uiState.value!

        // FlowWrapper.subscribe collects on Dispatchers.Main, so @Published can be set directly.
        stateHandle = viewModel.uiState.subscribe(onCollect: { [weak self] state in
            guard let state else { return }
            self?.state = state
        })
    }

    func onAction(_ action: Action) {
        _ = viewModel.onAction(action: action)
    }

    func onEvent(_ handler: @escaping (Event) -> Void) {
        eventHandle?.dispose()
        eventHandle = viewModel.event.subscribe(onCollect: { event in
            guard let event else { return }
            handler(event)
        })
    }

    deinit {
        stateHandle?.dispose()
        eventHandle?.dispose()
        provider.clear()
    }
}
