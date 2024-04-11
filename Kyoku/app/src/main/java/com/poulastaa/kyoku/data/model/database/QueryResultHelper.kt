package com.poulastaa.kyoku.data.model.database

data class AlbumPrevResult(
    val id: Long,
    val albumId: Long,
    val title: String,
    val artist: String,
    val coverImage: String,
    val name: String
)

data class ArtistPrevResult(
    val artistId: Long,
    val name: String,
    val artisUrl: String,
    val songId: Long,
    val title: String,
    val coverImage: String
)