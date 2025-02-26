package com.poulastaa.main.presentation.library

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.main.presentation.components.UiSavedItem

internal data class LibraryUiState(
    val noSavedData: Boolean = false,
    val filterType: UiLibraryFilterType = UiLibraryFilterType.ALL,
    val viewType: UiLibraryViewType = UiLibraryViewType.GRID,

    val playlist: List<UiSavedItem> = emptyList(),
    val album: List<UiSavedItem> = emptyList(),
    val artist: List<UiSavedItem> = emptyList(),
) {
    val canShowUi =
        playlist.isNotEmpty() || album.isNotEmpty() || artist.isNotEmpty() || noSavedData
}


internal enum class UiLibraryFilterType(
    @StringRes val id: Int,
    @DrawableRes val icon: Int,
) {
    PLAYLIST(R.string.playlist, R.drawable.ic_filter_playlist),
    ALBUM(R.string.album, R.drawable.ic_filter_album),
    ARTIST(R.string.artist, R.drawable.ic_filter_artist),
    ALL(R.string.all, R.drawable.ic_filter_all)
}

internal enum class UiLibraryViewType {
    GRID,
    LIST
}

internal enum class UiLibraryEditSavedItemType {
    ALBUM,
    PLAYLIST,
    ARTIST
}