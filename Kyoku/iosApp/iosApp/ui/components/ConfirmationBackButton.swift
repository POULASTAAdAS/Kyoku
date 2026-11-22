import SwiftUI

extension View {
    func confirmationBackButton(action: @escaping () -> Void) -> some View {
        navigationBarBackButtonHidden()
            .toolbar {
                ToolbarItem(placement: .topBarLeading) {
                    Button(
                        "Back",
                        systemImage: "chevron.backward",
                        action: action
                    )
                }
            }
    }
}
