package com.poulastaa.core.domain.model

data class DtoRelationSuggestedArtistSong(
    val artistId: ArtistId,
    val list: List<SongId>,
)
