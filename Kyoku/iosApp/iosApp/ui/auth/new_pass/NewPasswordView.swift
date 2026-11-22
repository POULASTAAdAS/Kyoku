//
//  NewPasswordView.swift
//  iosApp
//
//  Created by Poulastaa Das on 22/11/26.
//

import ComposeApp
import SwiftUI

struct NewPasswordView: View {
    @Environment(Router<AuthRoute>.self) private var router

    let email: String?
    let token: String?

    private enum Field: Hashable {
        case password
        case confirmPassword
    }

    @StateObject private var viewmodel = NewPasswordViewmodel()
    @Environment(\.verticalSizeClass) private var verticalSizeClass
    @FocusState private var focusField: Field?

    private var state: ResetPasswordUiState { viewmodel.state }

    private var passwordError: [String] {
        [state.password]
            .filter(\.isError)
            .compactMap(\.errorMessage)
    }

    private var confirmPasswordError: [String] {
        [state.confirmPassword]
            .filter(\.isError)
            .compactMap(\.errorMessage)
    }

    var body: some View {
        VStack(spacing: AppDimens.Spacing.medium) {
            header
                .padding(.horizontal, AppDimens.Layout.contentPadding)

            Form {
                Section {
                    passwordField
                    confirmPasswordField
                } footer: {
                    AuthSectionFooter(messages: passwordError)
                    AuthSectionFooter(messages: confirmPasswordError)
                }
                .alignmentGuide(.listRowSeparatorLeading) {
                    $0[.leading] + AppDimens.Icon.medium
                        + AppDimens.Spacing.medium
                }

                Section {
                    submitButton
                        .containerRelativeFrame(.horizontal) { w, _ in w * 0.7 }
                        .frame(maxWidth: .infinity)
                        .background(Color.clear)
                }
                .listRowBackground(Color.clear)
                .listRowInsets(EdgeInsets())
            }
            .environment(\.defaultMinListRowHeight, AppDimens.Component.control)
            .scrollContentBackground(.hidden)
            .background(Color.clear)
            .animation(.snappy, value: passwordError)
            .animation(.snappy, value: confirmPasswordError)
            .task {
                if let email { viewmodel.emailReceived(email) }
                if let token { viewmodel.tokenReceived(token) }
            }
            .onAppear(perform: bind)
        }
        .background(AppColors.background)
        .tint(AppColors.primary)

    }

    // MARK: - Sections
    private var header: some View {
        VStack(
            alignment: .leading,
            spacing: AppDimens.Spacing.small
        ) {
            Text("Create new password")
                .appTextStyle(.headlineMedium)
                .foregroundStyle(AppColors.primary)
                .accessibilityAddTraits(.isHeader)
        }
        .frame(
            maxWidth: .infinity,
            alignment: .leading
        )
    }

    private var passwordField: some View {
        AuthTextField(
            label: "Password",
            systemImage: "lock",
            value: state.password.value,
            onChange: viewmodel.passwordChange,
            isError: state.password.isError,
            field: .password,
            focus: $focusField,
            isSecure: true,
            isSecureTextVisible: state.isPasswordVisible,
            onToggleSecureTextVisibility: viewmodel.togglePasswordVisibility
        )
        .textContentType(.newPassword)
        .submitLabel(.next)
        .onSubmit {
            focusField = .confirmPassword
        }
    }

    private var confirmPasswordField: some View {
        AuthTextField(
            label: "Confirm password",
            systemImage: "lock",
            value: state.confirmPassword.value,
            onChange: viewmodel.confirmPasswordChange,
            isError: state.confirmPassword.isError,
            field: .confirmPassword,
            focus: $focusField,
            isSecure: true,
            isSecureTextVisible: state.isConfirmPasswordVisible,
            onToggleSecureTextVisibility: viewmodel
                .toggleConfirmPasswordVisibility
        )
        .textContentType(.newPassword)
        .submitLabel(.go)
        .onSubmit(submitPassword)
    }

    private var submitButton: some View {
        AuthActionButton(
            isLoading: state.isMakingApiCall,
            isEnabled: !state.password.value.isEmpty
                && !state.confirmPassword.value.isEmpty,
            action: submitPassword,
            label: {
                Text("Reset")
                    .appTextStyle(.titleMedium)
            }
        )
    }

    // MARK: - Wiring

    private func submitPassword() {
        focusField = nil
        viewmodel.resetPassword()
    }

    // MARK: - events

    private func bind() {
        viewmodel.onEvent { event in
            switch onEnum(of: event) {
            case .navigateBackToForgotPassword:
                navigateBackToForgotPassword()
            case .navigateToForgotPassword:
                router.popToRoot()
                router.push(.forgotPassword(email: nil))
            case .navigateToSignIn:
                router.popToRoot()
            }
        }
    }

    private func navigateBackToForgotPassword() {
        let destinationFound = router.popTo { route in
            if case .forgotPassword = route {
                return true
            }

            return false
        }

        guard !destinationFound else {
            return
        }

        // Fallback if New Password was opened outside the expected stack.
        router.popToRoot()
        router.push(.forgotPassword(email: state.email))
    }
}
