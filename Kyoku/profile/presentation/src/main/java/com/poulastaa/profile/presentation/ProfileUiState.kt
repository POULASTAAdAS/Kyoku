package com.poulastaa.profile.presentation

import com.poulastaa.core.presentation.designsystem.model.TextHolder
import com.poulastaa.core.presentation.designsystem.model.UiUser

internal data class ProfileUiState(
    val user: UiUser = UiUser(),
    val bDate: String = "",
    val username: TextHolder = TextHolder(),
    val savedItems: List<UiSavedItems> = emptyList(),
    val isUpdateUsernameBottomSheetVisible: Boolean = false,
    val isMakingApiCall: Boolean = false,
)

internal data class UiSavedItems(
    val itemCount: Int,
    val itemType: UiItemType,
)

internal enum class UiItemType {
    PLAYLIST,
    ALBUM,
    ARTIST,
    FAVOURITE
}