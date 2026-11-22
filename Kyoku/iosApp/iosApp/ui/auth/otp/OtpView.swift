//
//  OtpView.swift
//  iosApp
//
//  Created by Poulastaa Das on 20/11/26.
//

import ComposeApp
import SwiftUI

/// OTP verification screen. State and validation live in the shared `OtpViewmodel`; this view
/// renders the form, same layout as `ForgotPasswordView`, and maps `OtpUiEvent`s onto the router.
struct OtpView: View {
    @Environment(Router<AuthRoute>.self) private var router
    /// Email the code was sent to, passed from forgot-password like Compose `OtpScreen(email:)`.
    let email: String

    @StateObject private var viewModel = OtpViewModel()
    @EnvironmentObject private var commonUi: CommonUiStateObserver
    @FocusState private var focusedField: Field?

    private var state: OtpUiState { viewModel.state }
    private var isValidOtp: Bool {
        state.otp.value.count == Int(AuthInputValidationKt.OTP_LENGTH)
    }

    private var otpErrors: [String] {
        [state.otp]
            .filter(\.isError)
            .compactMap(\.errorMessage)
    }

    /// The viewmodel's "leave verification?" message carries a confirm action; iOS asks with an alert.
    private var leaveAction: SnackbarAction? { commonUi.status?.action }

    private enum Field: Hashable {
        case otp
    }

    var body: some View {
        VStack(spacing: AppDimens.Spacing.medium) {
            header
                .padding(.horizontal, AppDimens.Layout.contentPadding)

            Form {
                Section {
                    otpField
                } footer: {
                    AuthSectionFooter(messages: otpErrors)
                }
                // Each box is its own grouped cell, so the row itself has no background.
                .listRowBackground(Color.clear)
                .listRowInsets(EdgeInsets())

                Section {
                    AuthActionButton(
                        isLoading: state.isMakingApiCall,
                        isEnabled: isValidOtp,
                        action: submit
                    ) {
                        Text("Submit")
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
            .animation(.snappy, value: otpErrors)
        }
        .background(AppColors.background)
        .tint(AppColors.primary)
        // Back goes through the viewmodel, which asks before leaving (Compose `BackHandler`).
        .navigationBarBackButtonHidden()
        .toolbar {
            ToolbarItem(placement: .topBarLeading) {
                Button(
                    "Back",
                    systemImage: "chevron.backward",
                    action: viewModel.backClicked
                )
            }
        }
        .alert(
            commonUi.status?.message ?? "",
            isPresented: Binding(
                get: { leaveAction != nil },
                set: { if !$0 { commonUi.dismiss() } }
            ),
            presenting: leaveAction
        ) { action in
            Button(action.label, role: .destructive) {
                action.onClick()
                commonUi.dismiss()
            }
            Button("Cancel", role: .cancel) {}
        }
        .sensoryFeedback(.error, trigger: state.otp.isError) { _, isError in
            isError
        }
        .task(id: email) { viewModel.emailReceived(email) }
        .onAppear {
            bind()
            focusedField = .otp
        }
    }

    // MARK: - Sections

    private var header: some View {
        VStack(alignment: .leading, spacing: AppDimens.Spacing.small) {
            // Same title as Compose `OtpTopBar`, placed like `ForgotPasswordView`.
            Text("Verification")
                .appTextStyle(.headlineMedium)
                .foregroundStyle(AppColors.primary)
                .accessibilityAddTraits(.isHeader)

            Text(
                "Please enter the verification code we have sent to \(Text(email).fontWeight(.semibold))"
            )
            .appTextStyle(.bodySmall)
            .foregroundStyle(.secondary)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
    }

    private var otpField: some View {
        OtpCodeField(
            code: state.otp.value,
            length: Int(AuthInputValidationKt.OTP_LENGTH),
            isError: state.otp.isError,
            isFocused: focusedField == .otp,
            // The viewmodel keeps digits only, caps the length and validates once complete.
            onChange: viewModel.otpChanged
        )
        .focused($focusedField, equals: .otp)
        .disabled(state.isMakingApiCall)
    }

    // MARK: - Wiring

    private func submit() {
        focusedField = nil
        viewModel.validateOtp()
    }

    private func bind() {
        viewModel.onEvent { event in
            switch onEnum(of: event) {
            case .navigateToResetPassword(let event):
                router.push(
                    .newPassword(
                        email: event.email,
                        token: event.token
                    )
                )
                break
            case .navigateBack:
                router.pop()
            }
        }
    }
}
