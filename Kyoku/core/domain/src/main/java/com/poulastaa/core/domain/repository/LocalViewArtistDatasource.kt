package com.poulastaa.core.domain.repository

import com.poulastaa.core.domain.model.ArtistId

interface LocalViewArtistDatasource {
    suspend fun isArtistFollowed(artistId: ArtistId): Boolean
}