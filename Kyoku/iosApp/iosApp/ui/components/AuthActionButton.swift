import SwiftUI

/// Primary / secondary action button built on the system button styles, so it gets HIG sizing,
/// Dynamic Type, pressed and disabled states, and Liquid Glass on iOS 26 for free.
/// Shows a spinner in place of the label while `isLoading`, keeping the button's size unchanged.
struct AuthActionButton<Label: View>: View {
    enum Role { case primary, secondary }

    var role: Role = .primary
    let isLoading: Bool
    let isEnabled: Bool
    let action: () -> Void
    @ViewBuilder let label: () -> Label

    var body: some View {
        Button(action: action) {
            // The hidden label alone sizes the button; the spinner overlays it at regular size
            // (the button's `.large` control size would make it taller than the label).
            label()
                .opacity(isLoading ? 0 : 1)
                .overlay {
                    if isLoading {
                        ProgressView()
                            .controlSize(.regular)
                            .tint(role == .primary ? AppColors.onPrimary : nil)
                    }
                }
                // ~15% taller than the system `.large` button.
                .padding(.vertical, AppDimens.Spacing.extraSmall)
                .frame(maxWidth: .infinity)
        }
        .modifier(StyleModifier(role: role))
        .controlSize(.large)
        .disabled(!isEnabled)
        .animation(.default, value: isLoading)
    }

    private struct StyleModifier: ViewModifier {
        let role: Role

        func body(content: Content) -> some View {
            switch role {
            case .primary:
                content.modifier(ProminentStyle())
            case .secondary:
                content.modifier(SecondaryStyle())
            }
        }
    }

    private struct ProminentStyle: ViewModifier {
        func body(content: Content) -> some View {
            Group {
                if #available(iOS 26, *) {
                    content.buttonStyle(.glassProminent).buttonBorderShape(.capsule)
                } else {
                    content.buttonStyle(.borderedProminent).buttonBorderShape(.capsule)
                }
            }
            // The system's white label is unreadable on the light dark-mode `primary`; match M3 `onPrimary`.
            .foregroundStyle(AppColors.onPrimary)
        }
    }

    private struct SecondaryStyle: ViewModifier {
        func body(content: Content) -> some View {
            if #available(iOS 26, *) {
                content.buttonStyle(.glass).buttonBorderShape(.capsule)
            } else {
                content.buttonStyle(.bordered).buttonBorderShape(.capsule)
            }
        }
    }
}
