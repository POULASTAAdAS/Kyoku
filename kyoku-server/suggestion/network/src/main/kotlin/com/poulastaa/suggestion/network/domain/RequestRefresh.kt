package com.poulastaa.suggestion.network.domain

import com.poulastaa.core.domain.repository.AlbumId
import com.poulastaa.core.domain.repository.ArtistId
import com.poulastaa.core.domain.repository.SongId
import kotlinx.serialization.Serializable

@Serializable
internal data class RequestRefresh(
    val oldMostPopularSong: List<SongId> = emptyList(),
    val oldPopularArtistSongs: List<SongId> = emptyList(),
    val oldPopularYearSongs: List<SongId> = emptyList(),

    val oldSuggestedArtist: List<ArtistId> = emptyList(),
    val oldSuggestedAlbums: List<AlbumId> = emptyList(),

    val oldSuggestedArtistSongs: List<RequestSuggestedArtistSongRelation> = emptyList(),
)
