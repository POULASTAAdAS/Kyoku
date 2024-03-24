package com.poulastaa.kyoku.data.model.screens.library

import com.poulastaa.kyoku.data.model.screens.common.UiPlaylistPrev

data class LibraryUiState(
    val isInternetAvailable: Boolean = false,
    val isLoading: Boolean = true,
    val isInternetError: Boolean = true,
    val errorMessage: String = "Please Check Your Internet Connection.",
    val data: Data = Data()
)

data class Data(
    val data: LibraryUiData = LibraryUiData(),
    val pinned: LibraryUiData = LibraryUiData()
)

data class LibraryUiData(
    val isFavourite: Boolean = false,
    val playlist: List<UiPlaylistPrev> = emptyList(),
    val artist: List<String> = emptyList()
)
