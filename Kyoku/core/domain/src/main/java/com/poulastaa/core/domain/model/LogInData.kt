package com.poulastaa.core.domain.model

data class LogInData(
    val popularSongMixPrev: List<PrevSong> = emptyList(),
    val popularSongFromYourTimePrev: List<PrevSong> = emptyList(),
    val favouriteArtistMixPrev: List<PrevSong> = emptyList(),
    val dayTypeSong: List<PrevSong> = emptyList(),
    val popularAlbum: List<PrevAlbum> = emptyList(),
    val popularArtist: List<Artist> = emptyList(),
    val popularArtistSong: List<PrevArtistSong> = emptyList(),

    val savedPlaylist: List<PlaylistData> = emptyList(),
    val savedAlbum: List<PrevAlbum> = emptyList(),
    val savedArtist: List<Artist> = emptyList(),
)