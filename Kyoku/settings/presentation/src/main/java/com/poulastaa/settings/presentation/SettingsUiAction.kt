package com.poulastaa.settings.presentation

sealed interface SettingsUiAction {
    data object OnLogOutCLick : SettingsUiAction
}