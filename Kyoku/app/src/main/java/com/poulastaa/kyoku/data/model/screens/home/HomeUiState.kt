package com.poulastaa.kyoku.data.model.screens.home

import androidx.compose.runtime.Stable
import com.poulastaa.kyoku.data.model.api.service.home.HomeType
import com.poulastaa.kyoku.data.model.screens.common.UiPlaylistPrev

@Stable
data class HomeUiState(
    val isInternetAvailable: Boolean = false,
    val isLoading: Boolean = true,
    val isInternetError: Boolean = true,
    val errorMessage: String = "Please Check Your Internet Connection.",
    val isBottomSheetOpen: Boolean = false,
    val isBottomSheetLoading: Boolean = true,

    val dataType: HomeType = HomeType.NEW_USER_REQ,
    val data: HomeUiData = HomeUiData(),
    val bottomSheetData: BottomSheetData = BottomSheetData()
)

@Stable
data class HomeUiData(
    val fevArtistMixPrev: List<HomeUiFevArtistMix> = emptyList(),
    val albumPrev: List<HomeAlbumUiPrev> = emptyList(),
    val artistPrev: List<HomeUiArtistPrev> = emptyList(),
    val dailyMixPrevUrls: List<String> = emptyList(),
    val playlist: List<UiPlaylistPrev> = emptyList(),
    val historyPrev: List<HomeUiSongPrev> = emptyList(),
    val savedAlbumPrev: List<HomeUiSavedAlbumPrev> = emptyList(),
    val favourites: Boolean = false
)

@Stable
data class HomeUiFevArtistMix(
    val id: Long,
    val name: String,
    val coverImage: String
)

@Stable
data class HomeAlbumUiPrev(
    val id: Long,
    val name: String,
    val listOfSong: List<HomeUiSongPrev> = emptyList()
)

@Stable
data class HomeUiArtistPrev(
    val id: Long,
    val name: String,
    val artistCover: String,
    val lisOfPrevSong: List<HomeUiSongPrev> = emptyList()
)

@Stable
data class HomeUiSongPrev(
    val id: Long,
    val title: String,
    val artist: String,
    val coverImage: String
)

@Stable
data class HomeUiSavedAlbumPrev(
    val album: String,
    val coverImage: String,
)

@Stable
data class BottomSheetData(
    val id: Long = 0,
    val name: String = "",
    val urls: List<String> = emptyList(),
    val type: HomeLongClickType = HomeLongClickType.HISTORY_SONG,
    val isAlreadySaved: Boolean = false // used only for songs
)