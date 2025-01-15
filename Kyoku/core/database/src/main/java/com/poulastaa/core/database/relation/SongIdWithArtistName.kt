package com.poulastaa.core.database.relation

import com.poulastaa.core.domain.model.SongId

data class SongIdWithArtistName(
    val songId: SongId,
    val artist: String,
)
