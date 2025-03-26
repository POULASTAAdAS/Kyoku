package com.poulastaa.main.domain.model

import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.SongId

data class OldSuggestedArtistSongRelation(
    val artistId: ArtistId = -1,
    val prevSongs: List<SongId> = emptyList(),
)