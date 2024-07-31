package com.poulastaa.domain.model

data class SongWithArtistResult(
    val resultSong: ResultSong,
    val artistList: List<ResultArtist>,
)
