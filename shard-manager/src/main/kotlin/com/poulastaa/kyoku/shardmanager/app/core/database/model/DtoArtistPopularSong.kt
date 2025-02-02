package com.poulastaa.kyoku.shardmanager.app.core.database.model

data class DtoArtistPopularSong(
    val songId: Long,
    val artistId: Long,
    val countryId: Int,
)
