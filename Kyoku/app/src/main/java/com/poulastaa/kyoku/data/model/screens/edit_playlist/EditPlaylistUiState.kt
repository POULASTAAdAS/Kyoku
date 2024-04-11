package com.poulastaa.kyoku.data.model.screens.edit_playlist

import androidx.compose.runtime.Stable

@Stable
data class EditPlaylistUiState(
    val loadingStatus: LoadingStatus = LoadingStatus.LOADING,

    val searchText: String = "",
    val isSearchEnable: Boolean = false,

    val isInternetAvailable: Boolean = false,
    val isInternetError: Boolean = false,

    val isCookie: Boolean = false,
    val headerValue: String = "",

    val isNewPlaylist: Boolean = false,

    val data: List<EditPlaylistUiPlaylist> = emptyList()
)

@Stable
data class EditPlaylistUiPlaylist(
    val id: Long = 0,
    val name: String = "",
    val totalSongs: Int = 0,
    val isSelected: Boolean = false,
    val urls: List<String> = emptyList(),
)

enum class LoadingStatus {
    LOADING,
    NOT_LOADING,
    ERR
}