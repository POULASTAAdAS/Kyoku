package com.poulastaa.settings.presentation

sealed interface SettingsUiAction {
    data object ResetRevelAnimation : SettingsUiAction

    data object OpenDeleteAccountDialog : SettingsUiAction
    data object CancelDeleteAccountDialog : SettingsUiAction
    data object OnDeleteAccountDialog : SettingsUiAction

    data object OpenLogoutDialog : SettingsUiAction
    data object CancelLogoutDialog : SettingsUiAction
    data object OnLogoutDialog : SettingsUiAction

    data object OnToggleTheme : SettingsUiAction
    data object OnProfileClick : SettingsUiAction
    data object OnHistoryClick : SettingsUiAction
}