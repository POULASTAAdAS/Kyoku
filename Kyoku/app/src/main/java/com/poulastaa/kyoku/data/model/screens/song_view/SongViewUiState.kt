package com.poulastaa.kyoku.data.model.screens.song_view

import androidx.compose.runtime.Stable
import com.poulastaa.kyoku.data.model.api.service.home.SongPreview
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
    val favourites: UiFavourite = UiFavourite(),
    val artist: UiArtist = UiArtist(),
    val dailyMixOrArtistMix: UiDailyMixOrArtistMix = UiDailyMixOrArtistMix()
)

@Stable
data class UiPlaylist(
    val name: String = "",
    val totalTime: String = "",
    val listOfSong: List<UiPlaylistSong> = emptyList()
)

@Stable
data class UiAlbum(
    val name: String = "",
    val totalTime: String = "",
    val listOfSong: List<UiPlaylistSong> = emptyList()
)

@Stable
data class UiFavourite(
    val name: String = "",
    val totalTime: String = "",
    val listOfSong: List<UiPlaylistSong> = emptyList()
)


@Stable
data class UiArtist(
    val name: String = "",
    val coverImage: String = "",
    val points: Long = 0,
    val listOfSong: List<SongPreview> = emptyList()
)

data class UiDailyMixOrArtistMix(
    val name: String = "",
    val totalTime: String = "",
    val listOfSong: List<UiPlaylistSong> = emptyList()
)


@Stable
data class UiPlaylistSong(
    val songId: Long = 0,
    val title: String = "",
    val artist: String = "",
    val coverImage: String = "",
    val masterPlaylistUrl: String = "",
    val totalTime: String = ""
)


@Stable
data class UiSong(
    val id: Long = 0,
    val title: String = "",
    val artist: String = "",
    val album: String = "",
    val coverImage: String = ""
)



