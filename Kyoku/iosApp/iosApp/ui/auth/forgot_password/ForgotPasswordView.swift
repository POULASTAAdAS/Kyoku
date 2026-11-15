import ComposeApp
import SwiftUI

struct ForgotPasswordView: View {
    @Binding var path: [AuthRoute]

    @StateObject private var viewModel = ForgotPasswordViewModel()
    @Environment(\.verticalSizeClass) private var verticalSizeClass
    @FocusState private var focusedField: Field?

    private var state: ForgotPasswordUiState { viewModel.state }
    //    private var isBusy: Bool { state.isMakingApiCall || !state.isGetOtpEnabled }

    private enum Field: Hashable {
        case email
    }

    var body: some View {
        Form {
            Section {
                VStack(alignment: .leading, spacing: AppDimens.Spacing.small) {
                    Text("Reset password")
                        .appTextStyle(.titleLarge)
                        .foregroundStyle(.primary)

                    Text(
                        "Enter the email associated with your account and we will send an email with verification code to reset your password."
                    )
                    .appTextStyle(.bodySmall)
                    .foregroundStyle(.secondary)
                }
            }
            .listRowBackground(Color.clear)

            Section {
                AuthTextField(
                    label: "Email",
                    systemImage: "envelope",
                    value: state.email.value,
                    onChange: viewModel.emailChanged,
                    isError: state.email.isError,
                    field: .email,
                    focus: $focusedField
                )
                .textContentType(.emailAddress)
                .keyboardType(.emailAddress)
                .textInputAutocapitalization(.never)
                .autocorrectionDisabled()
                .submitLabel(.go)
                .onSubmit {
                    focusedField = nil
                    viewModel.getOtp(email: state.email.value)
                }
            }
        }
        .scrollContentBackground(.hidden)
        .background(AppColors.background)
        .tint(AppColors.primary)
    }
}
