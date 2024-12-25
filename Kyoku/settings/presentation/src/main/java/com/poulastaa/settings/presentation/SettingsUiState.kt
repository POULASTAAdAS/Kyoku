package com.poulastaa.settings.presentation

import com.poulastaa.core.presentation.ui.model.UiUser

data class SettingsUiState(
    val isDarkTheme: Boolean = false,
    val user: UiUser = UiUser(),
)
