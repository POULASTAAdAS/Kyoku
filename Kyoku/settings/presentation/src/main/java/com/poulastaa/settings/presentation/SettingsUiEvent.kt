package com.poulastaa.settings.presentation

import com.poulastaa.core.presentation.designsystem.UiText
import com.poulastaa.settings.domain.model.SettingsAllowedNavigationScreens

sealed interface SettingsUiEvent {
    data class EmitToast(val message: UiText) : SettingsUiEvent
    data object OnLogOutSuccess : SettingsUiEvent
    data class Navigate(val screen: SettingsAllowedNavigationScreens) : SettingsUiEvent
}