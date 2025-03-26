package com.poulastaa.suggestion.network.domain

import com.poulastaa.core.domain.repository.ArtistId
import com.poulastaa.core.domain.repository.SongId
import com.poulastaa.core.network.model.ResponsePrevArtist
import com.poulastaa.core.network.model.ResponsePrevSong
import kotlinx.serialization.Serializable

@Serializable
internal data class RequestSuggestedArtistSongRelation(
    val artistId: ArtistId = -1,
    val prevSongs: List<SongId> = emptyList(),
)
