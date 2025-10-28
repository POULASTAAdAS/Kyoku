package com.poulastaa.core.domain.model

import com.poulastaa.core.domain.utils.InternalId
import com.poulastaa.core.domain.utils.SongId

data class DtoPrevSong(
    val internalId: InternalId,
    val songId: SongId,
    val title: String,
    val artist: String?,
    val coverImage: String?,
)
