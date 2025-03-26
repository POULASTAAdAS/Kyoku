package com.poulastaa.suggestion.domain.model

import com.poulastaa.core.domain.repository.ArtistId
import com.poulastaa.core.domain.repository.SongId

data class OldSuggestedArtistSongRelation(
    val artistId: ArtistId = -1,
    val prevSongs: List<SongId> = emptyList(),
)
