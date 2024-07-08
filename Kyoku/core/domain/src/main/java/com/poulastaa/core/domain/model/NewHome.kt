package com.poulastaa.core.domain.model

data class NewHome(
    val status: ResponseStatus = ResponseStatus.FAILURE,
    val popularSongMixPrev: List<PrevSong> = emptyList(),
    val popularSongFromYourTimePrev: List<PrevSong> = emptyList(),
    val favouriteArtistMixPrev: List<PrevSong> = emptyList(),
    val dayTypeSong: List<PrevSong> = emptyList(),
    val popularAlbum: List<PrevAlbum> = emptyList(),
    val popularArtist: List<Artist> = emptyList(),
    val popularArtistSong: List<PreArtistSong> = emptyList(),
)
