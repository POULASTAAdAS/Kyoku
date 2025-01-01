package com.poulastaa.core.domain.model

import com.poulastaa.core.domain.repository.ArtistId
import com.poulastaa.core.domain.repository.SongId

data class RelationDtoSongArtist(
    val songId: SongId,
    val artistId: ArtistId,
)
