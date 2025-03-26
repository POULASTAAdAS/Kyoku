package com.poulastaa.main.network.model

import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.SongId
import kotlinx.serialization.Serializable

@Serializable
internal data class RequestSuggestedArtistSongRelation(
    val artistId: ArtistId = -1,
    val prevSongs: List<SongId> = emptyList(),
)