package com.poulastaa.core.domain.model

data class ViewArtistData(
    val followers: Long,
    val artist: Artist,
    val listOfSong: List<ViewArtistSong>,
)
