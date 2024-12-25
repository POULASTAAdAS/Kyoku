package com.poulastaa.settings.presentation

sealed interface SettingsUiEvent {
    data object OnLogOutSuccess : SettingsUiEvent
}