package com.poulastaa.core.domain.model

data class DtoSuggestedArtistSongRelation(
    val artistId: ArtistId = -1,
    val prevSongs: List<SongId> = emptyList(),
)