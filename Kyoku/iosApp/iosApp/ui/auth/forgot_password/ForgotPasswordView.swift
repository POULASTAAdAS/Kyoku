import ComposeApp
import SwiftUI

struct ForgotPasswordView: View {
    @Environment(Router<AuthRoute>.self) private var router
    /// Email typed on sign-in, if any; prefilled once like Compose `ForgotPasswordScreen`.
    let email: String?

    @StateObject private var viewModel = ForgotPasswordViewModel()
    @Environment(\.verticalSizeClass) private var verticalSizeClass
    @FocusState private var focusedField: Field?

    private var state: ForgotPasswordUiState { viewModel.state }

    private var credentialError: [String] {
        [state.email]
            .filter(\.isError)
            .compactMap(\.errorMessage)
    }

    private enum Field: Hashable {
        case email
    }

    var body: some View {
        VStack(spacing: AppDimens.Spacing.medium) {
            // Header outside the Form: the section-header slot has narrower margins than rows, which
            // wrapped the message early in landscape. Matches Compose's padded Column.
            header
                .padding(.horizontal, AppDimens.Layout.contentPadding)

            Form {
                Section {
                    emailField
                } footer: {
                    AuthSectionFooter(messages: credentialError)
                }
                .alignmentGuide(.listRowSeparatorLeading) {
                    $0[.leading] + AppDimens.Icon.medium
                        + AppDimens.Spacing.medium
                }

                Section {
                    AuthActionButton(
                        isLoading: state.isMakingApiCall,
                        isEnabled: state.isGetOtpEnabled,
                        action: submitOtp
                    ) {
                        Text("Get OTP")
                            .appTextStyle(.titleMedium)
                    }
                    .containerRelativeFrame(.horizontal) { w, _ in w * 0.7 }
                    .frame(maxWidth: .infinity)
                    .background(Color.clear)
                }
                .listRowBackground(Color.clear)
                .listRowInsets(EdgeInsets())

            }
            // Field rows match the action button height.
            .environment(\.defaultMinListRowHeight, AppDimens.Component.control)
            .scrollContentBackground(.hidden)
            .background(Color.clear)
            // Footer errors and the status row ease in and out instead of jumping the layout.
            .animation(.snappy, value: credentialError)

        }
        .background(AppColors.background)
        .tint(AppColors.primary)
        .task(id: email) {
            if let email { viewModel.emailChanged(email) }
        }
    }

    private var header: some View {
        VStack(alignment: .leading, spacing: AppDimens.Spacing.small) {
            // Same token as Compose `CompatVerticalForgotPasswordScreen`.
            Text("Reset password")
                .appTextStyle(.headlineMedium)
                .foregroundStyle(AppColors.primary)
                .accessibilityAddTraits(.isHeader)

            Text(
                "Enter the email associated with your account and we will send an email with verification code to reset your password."
            )
            .appTextStyle(.bodySmall)
            .foregroundStyle(.secondary)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
    }

    private var emailField: some View {
        AuthTextField(
            label: "Email",
            systemImage: "envelope",
            value: state.email.value,
            onChange: viewModel.emailChanged,
            isError: state.email.isError,
            field: .email,
            focus: $focusedField
        )
        // `.username` lets AutoFill offer the saved account, same as the sign-in email field.
        .textContentType(.username)
        .keyboardType(.emailAddress)
        .textInputAutocapitalization(.never)
        .autocorrectionDisabled()
        .submitLabel(.send)
        .onSubmit(submitOtp)
    }

    // MARK: - Wiring

    private func submitOtp() {
        focusedField = nil
        viewModel.getOtp()
    }
}
