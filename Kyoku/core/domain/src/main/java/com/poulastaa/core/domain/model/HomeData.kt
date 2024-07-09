package com.poulastaa.core.domain.model

data class HomeData(
    val popularSongMixPrev: List<PrevSong> = emptyList(),
    val popularSongFromYourTimePrev: List<PrevSong> = emptyList(),
    val favouriteArtistMixPrev: List<PrevSong> = emptyList(),
    val dayTypeSong: List<PrevSong> = emptyList(),
    val popularAlbum: List<PrevAlbum> = emptyList(),
    val suggestedArtist: List<Artist> = emptyList(),
    val popularArtistSong: List<PrevArtistSong> = emptyList(),
)
