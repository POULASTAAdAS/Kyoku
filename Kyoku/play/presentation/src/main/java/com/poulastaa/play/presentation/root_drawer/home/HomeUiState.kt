package com.poulastaa.play.presentation.root_drawer.home

import com.poulastaa.core.presentation.ui.model.UiArtist

data class HomeUiState(
    val heading: String = "",
    val isDataLoading: Boolean = true,
    val isNewUser: Boolean = true,

    val data: UiHomeData = UiHomeData(),
) {
    val canShowUi: Boolean
        get() = isDataLoading && !isNewUser
}

data class UiHomeData(
    val popularSongMix: List<UiPrevSong> = emptyList(),
    val popularSongFromYourTime: List<UiPrevSong> = emptyList(),
    val favouriteArtistMix: List<UiPrevSong> = emptyList(),
    val dayTypeSong: List<UiPrevSong> = emptyList(),
    val popularAlbum: List<UiPrevAlbum> = emptyList(),
    val suggestedArtist: List<UiArtist> = emptyList(),
    val popularArtistSong: List<UiArtistWithSong> = emptyList(),
)

data class UiPrevSong(
    val id: Long = -1,
    val coverImage: String = "",
)

data class UiPrevAlbum(
    val id: Long = -1,
    val name: String = "",
    val coverImage: String = "",
)

data class UiSongWithInfo(
    val id: Long = -1,
    val title: String = "",
    val coverImage: String = "",
)

data class UiArtistWithSong(
    val artist: UiArtist = UiArtist(),
    val listOfSong: List<UiSongWithInfo> = emptyList(),
)