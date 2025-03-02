package com.poulastaa.profile.presentation

import com.poulastaa.core.presentation.designsystem.model.TextHolder
import com.poulastaa.core.presentation.designsystem.model.UiUser

internal data class ProfileUiState(
    val user: UiUser = UiUser(
        username = "Poulastaa",
        email = "poulastaadas2@gmail.com"
    ),
    val bDate: String = "10-10-2025",
    val username: TextHolder = TextHolder(),
    val savedItems: List<UiSavedItems> = listOf(
        UiSavedItems(
            itemType = UiItemType.PLAYLIST,
            itemCount = 10
        ),
        UiSavedItems(
            itemType = UiItemType.ARTIST,
            itemCount = 10
        ),
        UiSavedItems(
            itemType = UiItemType.ALBUM,
            itemCount = 10
        ),
        UiSavedItems(
            itemType = UiItemType.FAVOURITE,
            itemCount = 10
        )
    ),
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