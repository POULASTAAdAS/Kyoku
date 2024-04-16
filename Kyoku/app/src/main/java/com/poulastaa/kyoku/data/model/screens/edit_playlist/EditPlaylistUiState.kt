package com.poulastaa.kyoku.data.model.screens.edit_playlist

import androidx.compose.runtime.Stable

@Stable
data class EditPlaylistUiState(
    val songId: Long = -1L,

    val loadingStatus: LoadingStatus = LoadingStatus.LOADING,

    val searchText: String = "",
    val isSearchEnable: Boolean = false,

    val isInternetAvailable: Boolean = false,
    val isInternetError: Boolean = false,

    val isCookie: Boolean = false,
    val headerValue: String = "",

    val isNewPlaylist: Boolean = false,
    val newPlaylistText: String = "",

    val isMakingApiCall: Boolean = false,

    val isNavigateBack: Boolean = false,

    val addList: List<Long> = emptyList(),
    val playlist: List<EditPlaylistUiPlaylist> = emptyList(),
    val fav: UiFav = UiFav()
)

@Stable
data class EditPlaylistUiPlaylist(
    val id: Long = 0,
    val name: String = "",
    val totalSongs: Int = 0,
    val isSelected: Boolean = false,
    val urls: List<String> = emptyList(),
)

@Stable
enum class LoadingStatus {
    LOADING,
    NOT_LOADING,
    ERR
}

@Stable
data class UiFav(
    val isSelected: Boolean = false,
    val totalSongs: Int = 0
)