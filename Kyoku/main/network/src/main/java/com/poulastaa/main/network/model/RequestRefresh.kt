package com.poulastaa.main.network.model

import com.poulastaa.core.domain.model.AlbumId
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.SongId
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
