package com.poulastaa.suggestion.network.model

import com.poulastaa.core.domain.repository.ArtistId
import com.poulastaa.core.domain.repository.SongId
import kotlinx.serialization.Serializable

@Serializable
internal data class RequestSuggestedArtistSongRelation(
    val artistId: ArtistId = -1,
    val prevSongs: List<SongId> = emptyList(),
)
