package com.poulastaa.kyoku.data.model.screens.library

import androidx.compose.runtime.Stable
import com.poulastaa.kyoku.data.model.screens.common.UiPlaylistPrev

@Stable
data class LibraryUiState(
    val isInternetAvailable: Boolean = false,
    val isLoading: Boolean = true,
    val isInternetError: Boolean = true,
    val errorMessage: String = "Please Check Your Internet Connection.",
    val isGrid: Boolean = true,
    val maxGridSize: Int = 3,
    val minGridSize: Int = 1,
    val isDialogOpen: Boolean = false,
    val isBottomSheetOpen: Boolean = false,
    val data: Data = Data(),
    val pinnedData: PinnedData = PinnedData(),
    val filterChip: FilterChip = FilterChip()
)

@Stable
data class Data(
    val all: LibraryUiData = LibraryUiData(),
    val pinned: LibraryUiData = LibraryUiData()
)

@Stable
data class LibraryUiData(
    val isFavourite: Boolean = false,
    val playlist: List<UiPlaylistPrev> = emptyList(),
    val album: List<UiAlbum> = emptyList(),
    val artist: List<Artist> = emptyList()
)

@Stable
data class UiAlbum(
    val id: Long,
    val name: String,
    val points: Int
)

@Stable
data class Artist(
    val id: Long,
    val name: String,
    val imageUrl: String
)

@Stable
data class PinnedData(
    val name: String = "",
    val type: String = "",
    val isPinned: Boolean = false
)

@Stable
data class FilterChip(
    val isPlaylist: Boolean = false,
    val isAlbum: Boolean = false,
    val isArtist: Boolean = false
)

@Stable
enum class PinnedDataType {
    ARTIST,
    PLAYLIST,
    FAVOURITE,
    ALBUM
}