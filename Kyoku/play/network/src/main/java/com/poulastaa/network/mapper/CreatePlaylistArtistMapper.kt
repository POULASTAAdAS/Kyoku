package com.poulastaa.network.mapper

import com.poulastaa.core.domain.model.ArtistSingleData
import com.poulastaa.core.domain.model.CreatePlaylistPagingData

fun ArtistSingleData.toCreatePlaylistPagingData() = CreatePlaylistPagingData(
    id = id,
    title = title,
    coverImage = coverImage,
    artist = "",
    expandable = true,
    isArtist = false
)