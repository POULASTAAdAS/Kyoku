package com.poulastaa.play.presentation.settings

sealed interface SettingUiEvent {
    data object OnLogOutClick : SettingUiEvent
    data object OnLogOutConform : SettingUiEvent
    data object OnLogOutCancel : SettingUiEvent
}