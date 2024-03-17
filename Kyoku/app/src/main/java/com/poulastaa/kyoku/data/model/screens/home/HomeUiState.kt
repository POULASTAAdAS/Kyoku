package com.poulastaa.kyoku.data.model.screens.home

data class HomeUiState(
    val isInternetAvailable: Boolean = false,
    val isLoading: Boolean = true,

    val data: HomeUiData = HomeUiData()
)

data class HomeUiData(
    val fevArtistMixPrev: List<HomeUiFevArtistMix> = emptyList(),
    val albumPrev: List<HomeAlbumUiPrev> = emptyList(),
    val artistPrev: List<HomeUiArtistPrev> = emptyList(),
    val dailyMixPrev: HomeUiDailyMixPrev = HomeUiDailyMixPrev(),
    val playlist: List<String> = emptyList(), // todo change
    val favourites: List<String> = emptyList(), // todo change
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
    val id: Long,
    val name: String,
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

