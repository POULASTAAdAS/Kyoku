package com.poulastaa.kyoku.data.model.screens.song_view

import androidx.compose.runtime.Stable
import com.poulastaa.kyoku.data.model.screens.common.ItemsType

@Stable
data class SongViewUiState(
    val isLoading: Boolean = true,
    val isInternetAvailable: Boolean = false,
    val isInternetError: Boolean = false,
    val type: ItemsType = ItemsType.ERR,
    val data: SongViewData = SongViewData()
)

@Stable
data class SongViewData(
    val playlist: UiPlaylist = UiPlaylist()
)

data class UiPlaylist(
    val name: String = "",
    val listOfSong: List<UiPlaylistSong> = emptyList()
)

data class UiPlaylistSong(
    val name: String = "",
    val id: Long = 0,
    val title: String = "",
    val artist: String = "",
    val album: String = "",
    val coverImage: String = ""
    // todo may need to add more info
)
