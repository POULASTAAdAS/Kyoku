package com.poulastaa.core.database.model

data class PopularArtistWithSongResult(
    val artistId: Long,
    val name: String,
    val artistCover: String,

    val songId: Long,
    val title: String,
    val coverImage: String,
)
