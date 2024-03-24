package com.poulastaa.kyoku.data.model.screens.home

import com.poulastaa.kyoku.data.model.api.service.home.HomeType
import com.poulastaa.kyoku.data.model.screens.common.UiPlaylistPrev

data class HomeUiState(
    val isInternetAvailable: Boolean = false,
    val isLoading: Boolean = true,
    val isInternetError: Boolean = true,
    val errorMessage: String = "Please Check Your Internet Connection.",

    val isHome: Boolean = true,

    val dataType: HomeType = HomeType.NEW_USER_REQ,
    val data: HomeUiData = HomeUiData()
)

data class HomeUiData(
    val fevArtistMixPrev: List<HomeUiFevArtistMix> = emptyList(),
    val albumPrev: List<HomeAlbumUiPrev> = emptyList(),
    val artistPrev: List<HomeUiArtistPrev> = emptyList(),
    val dailyMixPrev: HomeUiDailyMixPrev = HomeUiDailyMixPrev(),
    val playlist: List<UiPlaylistPrev> = emptyList(),
    val historyPrev: List<HomeUiSongPrev> = emptyList(),
    val savedAlbumPrev: List<HomeUiSavedAlbumPrev> = emptyList(),
    val favourites: Boolean = false
)

data class HomeUiFevArtistMix(
    val id: Long,
    val name: String,
    val coverImage: String
)

data class HomeAlbumUiPrev(
    val name: String,
    val listOfSong: List<HomeUiSongPrev> = emptyList()
)

data class HomeUiArtistPrev(
    val name: String,
    val artistCover: String,
    val lisOfPrevSong: List<HomeUiSongPrev> = emptyList()
)

data class HomeUiSongPrev(
    val id: Long,
    val title: String,
    val coverImage: String,
    val artist: String
)

data class HomeUiDailyMixPrev(
    val listOfSong: List<HomeUiSongPrev> = emptyList()
)



data class HomeUiSavedAlbumPrev(
    val coverImage: String,
    val album: String
)

