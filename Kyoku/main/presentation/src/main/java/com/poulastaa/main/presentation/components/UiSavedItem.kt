package com.poulastaa.main.presentation.components

import androidx.compose.runtime.Stable
import com.poulastaa.core.domain.model.PlaylistId

@Stable
internal data class UiSavedItem(
    val id: PlaylistId = -1,
    val name: String = "",
    val posters: List<String> = emptyList(),
    val type: UiSaveItemType? = null,
)

internal enum class UiSaveItemType {
    PLAYLIST,
    ALBUM,
    ARTIST
}
