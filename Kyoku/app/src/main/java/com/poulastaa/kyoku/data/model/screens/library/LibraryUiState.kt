package com.poulastaa.kyoku.data.model.screens.library

import com.poulastaa.kyoku.data.model.screens.common.UiPlaylistPrev

data class LibraryUiState(
    val isInternetAvailable: Boolean = false,
    val isLoading: Boolean = true,
    val isGrid: Boolean = true,
    val maxGridSize: Int = 3,
    val minGridSize: Int = 1,
    val isInternetError: Boolean = false,
    val errorMessage: String = "Please Check Your Internet Connection.",
    val data: Data = Data()
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
