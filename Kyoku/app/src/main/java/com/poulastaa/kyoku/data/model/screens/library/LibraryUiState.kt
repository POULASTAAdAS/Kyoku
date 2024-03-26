package com.poulastaa.kyoku.data.model.screens.library

import com.poulastaa.kyoku.data.model.screens.common.UiPlaylistPrev

data class LibraryUiState(
    val isInternetAvailable: Boolean = false,
    val isLoading: Boolean = true,
    val isInternetError: Boolean = false,
    val errorMessage: String = "Please Check Your Internet Connection.",
    val isGrid: Boolean = true,
    val maxGridSize: Int = 3,
    val minGridSize: Int = 1,
    val isBottomSheetOpen: Boolean = false,
    val data: Data = Data(),
    val pinnedData: PinnedData = PinnedData()
)

data class Data(
    val all: LibraryUiData = LibraryUiData(),
    val pinned: LibraryUiData = LibraryUiData()
)

data class LibraryUiData(
    val isFavourite: Boolean = false,
    val playlist: List<UiPlaylistPrev> = emptyList(),
    val artist: List<Artist> = emptyList()
)

data class Artist(
    val id: Long,
    val name: String,
    val imageUrl: String
)

data class PinnedData(
    val name: String = "",
    val type: String = "",
    val isPinned: Boolean = false
)

enum class PinnedDataType {
    ARTIST,
    PLAYLIST,
    FAVOURITE,
    ALBUM
}