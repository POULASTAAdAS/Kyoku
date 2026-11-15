import SwiftUI

/// One row of a credential `Form` section: leading SF Symbol, text or secure field, optional
/// trailing visibility toggle. The `Form` supplies the inset-grouped cell, separators and heights;
/// validation text belongs in the section footer (see `AuthSectionFooter`), not in the row.
/// The value lives in the shared viewmodel, so the row reports edits through `onChange`.
struct AuthTextField<Field: Hashable>: View {
    let label: String
    let systemImage: String
    let value: String
    let onChange: (String) -> Void
    let isError: Bool
    let field: Field
    let focus: FocusState<Field?>.Binding
    var isSecure = false
    var isSecureTextVisible = false
    var onToggleSecureTextVisibility: (() -> Void)? = nil

    var body: some View {
        HStack(spacing: AppDimens.Spacing.medium) {
            Image(systemName: systemImage)
                .symbolRenderingMode(.hierarchical)
                .foregroundStyle(isError ? AppColors.error : AppColors.primary)
                .frame(width: AppDimens.Icon.medium)
                .accessibilityHidden(true)

            input
                .focused(focus, equals: field)

            if isSecure, let onToggleSecureTextVisibility {
                Button(action: onToggleSecureTextVisibility) {
                    Image(systemName: isSecureTextVisible ? "eye.slash" : "eye")
                        .symbolRenderingMode(.hierarchical)
                        .contentTransition(.symbolEffect(.replace))
                }
                .buttonStyle(.borderless)
                .foregroundStyle(.secondary)
                .accessibilityLabel(isSecureTextVisible ? "Hide password" : "Show password")
            }
        }
    }

    @ViewBuilder
    private var input: some View {
        // Text and binding are shared, so toggling visibility keeps the typed value.
        let text = Binding(get: { value }, set: onChange)

        if isSecure && !isSecureTextVisible {
            SecureField(label, text: text)
        } else {
            TextField(label, text: text)
        }
    }
}

/// Section footer listing validation errors, the iOS placement for form feedback.
/// Renders nothing when there are no messages so the section keeps its default spacing.
struct AuthSectionFooter: View {
    let messages: [String]

    var body: some View {
        if !messages.isEmpty {
            VStack(alignment: .leading, spacing: AppDimens.Spacing.extraSmall) {
                ForEach(messages, id: \.self) { message in
                    Label(message, systemImage: "exclamationmark.circle.fill")
                }
            }
            .foregroundStyle(AppColors.error)
        }
    }
}

#Preview("Credential section, error state") {
    @Previewable @FocusState var focus: String?

    Form {
        Section {
            AuthTextField(
                label: "Email", systemImage: "envelope", value: "someone@", onChange: { _ in },
                isError: true, field: "email", focus: $focus
            )
            AuthTextField(
                label: "Password", systemImage: "lock", value: "abc", onChange: { _ in },
                isError: true, field: "password", focus: $focus,
                isSecure: true, onToggleSecureTextVisibility: {}
            )
        } footer: {
            AuthSectionFooter(messages: ["Invalid email", "Use 8+ characters"])
        }
        .alignmentGuide(.listRowSeparatorLeading) { $0[.leading] + AppDimens.Icon.medium + AppDimens.Spacing.medium }
    }
    .scrollContentBackground(.hidden)
    .background(AppColors.background)
    .tint(AppColors.primary)
}
