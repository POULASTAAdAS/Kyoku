package com.poulastaa.core.domain.model

data class AlbumWithSong(
    val album: PrevAlbum,
    val listOfSong: List<Song>,
)
