package com.poulastaa.kyoku.data.model.screens.song_view

import androidx.compose.runtime.Stable

@Stable
data class ArtistAllUiState(
    val isInternetAvailable: Boolean = false,
    val isLoading: Boolean = true,
    val artistUrl: String = "",
    val isInternetError: Boolean = true,
    val errorMessage: String = "Please Check Your Internet Connection.",
    val isCooke: Boolean = false,
    val headerValue: String = "",

    val isBottomSheetOpen: Boolean = false,
    val bottomSheetData: BottomSheetData = BottomSheetData()
)

data class BottomSheetData(
    val url: String = "",
    val id: Long = -1,
    val name: String = "",
    val type: BottomSheetDataType = BottomSheetDataType.SONG,
    val operation: Boolean = false //if true then positive
) {
    enum class BottomSheetDataType {
        ALBUM,
        SONG
    }
}


