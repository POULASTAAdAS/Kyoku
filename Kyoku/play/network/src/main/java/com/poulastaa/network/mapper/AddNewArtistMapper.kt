package com.poulastaa.network.mapper

import com.poulastaa.core.domain.model.ArtistPagingType
import com.poulastaa.network.model.ArtistPagingTypeDto


fun ArtistPagingType.toArtistPagingTypeDto() = when (this) {
    ArtistPagingType.ALL -> ArtistPagingTypeDto.ALL
    ArtistPagingType.INTERNATIONAL -> ArtistPagingTypeDto.INTERNATIONAL
}