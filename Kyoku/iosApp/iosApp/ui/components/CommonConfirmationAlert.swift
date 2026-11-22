import ComposeApp
import SwiftUI

extension View {
    func commonConfirmationAlert(_ commonUi: CommonUiStateObserver) -> some View {
        modifier(CommonConfirmationAlertModifier(commonUi: commonUi))
    }
}

private struct CommonConfirmationAlertModifier: ViewModifier {
    @ObservedObject var commonUi: CommonUiStateObserver

    private var action: SnackbarAction? { commonUi.status?.action }

    func body(content: Content) -> some View {
        content.alert(
            commonUi.status?.message ?? "",
            isPresented: Binding(
                get: { action != nil },
                set: { if !$0 { commonUi.dismiss() } }
            ),
            presenting: action
        ) { action in
            Button(action.label, role: .destructive) {
                action.onClick()
                commonUi.dismiss()
            }
            Button("Cancel", role: .cancel) {}
        }
    }
}
