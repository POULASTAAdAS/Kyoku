package com.poulastaa.core.domain.repository.explore_artist

import com.poulastaa.core.domain.model.Artist
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.Result

interface ExploreArtistRepository {
    suspend fun getArtist(artistId: Long): Result<Artist, DataError.Network>

    suspend fun isArtistFollowed(artistId: Long): Boolean

    suspend fun followArtist(artistId: Long): EmptyResult<DataError.Network>
    suspend fun unFollowArtist(artistId: Long): EmptyResult<DataError.Network>
}