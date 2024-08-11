package com.poulastaa.play.presentation.settings

data class SettingUiState(
    val profilePicUrl: String = "",
    val header: String = "",

    val isLogoutDialogVisible: Boolean = false,
    val isLoggingOut: Boolean = false,
)
