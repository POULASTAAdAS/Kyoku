package com.poulastaa.view.presentation.others

import com.poulastaa.core.presentation.designsystem.model.ItemClickType
import com.poulastaa.core.presentation.designsystem.model.PlayType

internal sealed interface ViewOtherUiAction {
    data object OnMenuClick : ViewOtherUiAction

    data class OnPlayAll(val playType: PlayType) : ViewOtherUiAction
    data class OnSongClick(val type: ItemClickType, val songId: Long) : ViewOtherUiAction
    data class OnSongMenuToggle(val songId: Long) : ViewOtherUiAction

    data object OnCheckOutClick : ViewOtherUiAction
}