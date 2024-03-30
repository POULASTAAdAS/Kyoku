package com.poulastaa.kyoku.data.model.screens.song_view

import androidx.compose.runtime.Stable
import com.poulastaa.kyoku.data.model.screens.common.ItemsType

@Stable
data class SongViewUiState(
    val isCooke: Boolean = false,
    val headerValue: String = "",
    val isLoading: Boolean = true,
    val isInternetAvailable: Boolean = false,
    val isInternetError: Boolean = false,
    val type: ItemsType = ItemsType.ERR,
    val data: SongViewData = SongViewData()
)

@Stable
data class SongViewData(
    val playlist: UiPlaylist = UiPlaylist(),
    val album: UiAlbum = UiAlbum(),
    val favourites: List<UiSong> = emptyList(),
)

@Stable
data class UiPlaylist(
    val name: String = "",
    val listOfSong: List<UiPlaylistSong> = emptyList()
)

@Stable
data class UiAlbum(
    val name: String = "",
    val listOfSong: List<UiSong> = emptyList()
)

@Stable
data class UiPlaylistSong(
    val id: Long = 0,
    val name: String = "",
    val title: String = "",
    val artist: String = "",
    val album: String = "",
    val coverImage: String = ""
)


@Stable
data class UiSong(
    val id: Long = 0,
    val title: String = "",
    val artist: String = "",
    val album: String = "",
    val coverImage: String = ""
)



