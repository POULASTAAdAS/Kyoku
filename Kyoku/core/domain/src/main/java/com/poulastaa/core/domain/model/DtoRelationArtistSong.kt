package com.poulastaa.core.domain.model

data class DtoRelationArtistSong(
    val artistId: ArtistId,
    val list: List<SongId>,
)
