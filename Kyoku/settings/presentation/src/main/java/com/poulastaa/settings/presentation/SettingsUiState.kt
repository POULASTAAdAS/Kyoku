package com.poulastaa.settings.presentation

import com.poulastaa.core.presentation.designsystem.model.UiUser

data class SettingsUiState(
    val isDarkTheme: Boolean = false,
    val user: UiUser = UiUser(),
)
