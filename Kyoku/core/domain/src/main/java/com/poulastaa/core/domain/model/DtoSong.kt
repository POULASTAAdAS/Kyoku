package com.poulastaa.core.domain.model

import com.poulastaa.core.domain.utils.InternalId
import com.poulastaa.core.domain.utils.SongId

data class DtoSong(
    val internalId: InternalId = -1,
    val songId: SongId,
    val title: String,
    val coverImage: String?,
    val artists: List<DtoArtist>,
)
