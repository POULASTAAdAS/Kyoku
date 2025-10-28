package com.poulastaa.core.domain.model

import com.poulastaa.core.domain.utils.ArtistId
import com.poulastaa.core.domain.utils.InternalId

data class DtoArtist(
    val internalId: InternalId = -1,
    val artistId: ArtistId,
    val name: String,
    val coverImage: String?,
    val popularity: Long,
)
