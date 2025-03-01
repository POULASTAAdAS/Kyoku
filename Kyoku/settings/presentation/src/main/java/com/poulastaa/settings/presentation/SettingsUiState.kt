package com.poulastaa.settings.presentation

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.toOffset
import com.poulastaa.core.presentation.designsystem.model.UiUser

data class SettingsUiState(
    val isLogoutDialogVisible: Boolean = false,
    val isDeleteAccountDialogVisible: Boolean = false,
    val offset: Offset = IntOffset(0, 0).toOffset(),
    val themChangeAnimationTime: Int = 800,
    val user: UiUser = UiUser(),
)
