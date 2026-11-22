import ComposeApp

typealias RootViewModel = ObservableViewModel<
    RootUiState, any RootUiAction, KotlinNothing
>

extension ObservableViewModel
where
    State == RootUiState, Action == any RootUiAction,
    Event == KotlinNothing
{
    convenience init() {
        self.init {
            $0.rootViewmodel()
                as! BaseViewmodel<State, any RootUiAction, KotlinNothing>
        }
    }

    func destinationChanged(_ screen: any Screens) {
        onAction(RootUiActionOnDestinationChanged(screen: screen))
    }
}
