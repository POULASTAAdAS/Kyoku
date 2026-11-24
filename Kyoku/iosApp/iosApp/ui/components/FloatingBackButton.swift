import SwiftUI

extension View {
    /// Hides the navigation bar and floats a back button over the content, so a pushed screen's
    /// content starts at the top like the stack root instead of being inset by the bar's height.
    func floatingBackButton(action: @escaping () -> Void) -> some View {
        toolbar(.hidden, for: .navigationBar)
            .overlay(alignment: .topLeading) {
                FloatingBackButton(action: action)
            }
    }
}

private struct FloatingBackButton: View {
    let action: () -> Void

    var body: some View {
        Button("Back", systemImage: "chevron.backward", action: action)
            .labelStyle(.iconOnly)
            // Match the system bar back button: semibold body-size chevron in a large control.
            .font(.title2.weight(.semibold))
            // The system bar draws its chevron at large scale.
            .imageScale(.large)
            .controlSize(.large)
            .modifier(Style())
            .padding(.leading, AppDimens.Layout.contentPadding)
    }

    private struct Style: ViewModifier {
        func body(content: Content) -> some View {
            if #available(iOS 26, *) {
                content.buttonStyle(.glass).buttonBorderShape(.circle)
            } else {
                content.buttonStyle(.bordered).buttonBorderShape(.circle)
            }
        }
    }
}
