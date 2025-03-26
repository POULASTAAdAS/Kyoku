package com.poulastaa.suggestion.domain.model

import com.poulastaa.core.domain.repository.AlbumId
import com.poulastaa.core.domain.repository.ArtistId
import com.poulastaa.core.domain.repository.SongId

data class OldRefresh(
    val oldMostPopularSong: List<SongId> = emptyList(),
    val oldPopularArtistSongs: List<SongId> = emptyList(),
    val oldPopularYearSongs: List<SongId> = emptyList(),

    val oldSuggestedArtist: List<ArtistId> = emptyList(),
    val oldSuggestedAlbums: List<AlbumId> = emptyList(),

    val oldSuggestedArtistSongs: List<OldSuggestedArtistSongRelation> = emptyList(),
)
