//
//  NewPasswordViewmodel.swift
//  iosApp
//
//  Created by Poulastaa Das on 22/11/26.
//

import ComposeApp

typealias NewPasswordViewmodel = ObservableViewModel<
    ResetPasswordUiState, any ResetPasswordUiAction, any ResetPasswordUiEvent
>

extension ObservableViewModel
where
    State == ResetPasswordUiState, Action == any ResetPasswordUiAction,
    Event == any ResetPasswordUiEvent
{
    convenience init() {
        self.init { $0.resetPasswordViewmodel() }
    }

    // MARK: - Arguments from navigation

    func tokenReceived(_ token: String) {
        onAction(ResetPasswordUiActionOnTokenReceived(token: token))
    }

    func emailReceived(_ email: String) {
        onAction(ResetPasswordUiActionOnEmailReceived(email: email))
    }

    // MARK: - Input

    func passwordChange(_ password: String) {
        onAction(ResetPasswordUiActionOnPasswordChange(password: password))
    }

    func confirmPasswordChange(_ password: String) {
        onAction(
            ResetPasswordUiActionOnConfirmPasswordChange(password: password)
        )
    }

    // MARK: - Toggles

    func togglePasswordVisibility() {
        onAction(ResetPasswordUiActionOnPasswordVisibilityToggle.shared)
    }

    func toggleConfirmPasswordVisibility() {
        onAction(ResetPasswordUiActionOnConfirmPasswordVisibilityToggle.shared)
    }

    // MARK: - Submit / navigation

    func resetPassword() {
        onAction(ResetPasswordUiActionResetPassword.shared)
    }

    func backClick() {
        onAction(ResetPasswordUiActionOnBackClick.shared)
    }
}
