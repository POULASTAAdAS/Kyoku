package com.poulastaa.settings.presentation

import androidx.compose.ui.geometry.Offset
import com.poulastaa.core.domain.model.ThemColor

sealed interface SettingsUiAction {
    data object ResetRevelAnimation : SettingsUiAction

    data object OpenDeleteAccountDialog : SettingsUiAction
    data object CancelDeleteAccountDialog : SettingsUiAction
    data object OnDeleteAccountDialog : SettingsUiAction

    data object OpenLogoutDialog : SettingsUiAction
    data object CancelLogoutDialog : SettingsUiAction
    data object OnLogoutDialog : SettingsUiAction

    data class OnStartThemChange(val offset: Offset) : SettingsUiAction
    data object OnProfileClick : SettingsUiAction
    data object OnHistoryClick : SettingsUiAction

    data object OnThemPicToggle : SettingsUiAction
    data class OnThemChange(val them: ThemColor) : SettingsUiAction

    data class OnDragOffset(val x: Int) : SettingsUiAction
}