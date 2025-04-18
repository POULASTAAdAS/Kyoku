package com.poulastaa.settings.presentation

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.toOffset
import com.poulastaa.core.presentation.designsystem.model.UiUser

data class SettingsUiState(
    val isLoading: Boolean = false,
    val isLogoutBottomSheetVisible: Boolean = false,
    val isDeleteAccountVBottomSheetVisible: Boolean = false,
    val isThemChangeScreenVisible: Boolean = false,
    val offset: Offset = IntOffset(0, 0).toOffset(),
    val dragOffset: IntOffset = IntOffset(0, 0),
    val themChangeAnimationTime: Int = 800,
    val user: UiUser = UiUser(),
)
