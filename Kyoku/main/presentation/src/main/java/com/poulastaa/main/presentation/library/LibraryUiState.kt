package com.poulastaa.main.presentation.library

import com.poulastaa.main.presentation.components.UiSavedItem

internal data class LibraryUiState(
    val filterType: FilterType = FilterType.ALL,
    val noSavedData: Boolean = false,

    val playlist: List<UiSavedItem> = emptyList(),
    val album: List<UiSavedItem> = emptyList(),
    val artist: List<UiSavedItem> = emptyList(),
) {
    val canShowUi =
        playlist.isNotEmpty() || album.isNotEmpty() || artist.isNotEmpty() || noSavedData
}


internal enum class FilterType {
    PLAYLIST,
    ALBUM,
    ARTIST,
    ALL
}