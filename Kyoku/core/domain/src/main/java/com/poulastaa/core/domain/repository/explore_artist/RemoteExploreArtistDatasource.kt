package com.poulastaa.core.domain.repository.explore_artist

import com.poulastaa.core.domain.model.Artist
import com.poulastaa.core.domain.model.ArtistSingleData
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.Result

interface RemoteExploreArtistDatasource {
    suspend fun getArtist(artistId: Long): Result<Artist, DataError.Network>

    suspend fun followArtist(artistId: Long): Result<Artist, DataError.Network>
    suspend fun unFollowArtist(artistId: Long): EmptyResult<DataError.Network>

    suspend fun getArtistSong(
        artistId: Long,
        page: Int,
        size: Long
    ): Result<List<ArtistSingleData>, DataError.Network>

    suspend fun getArtistAlbum(
        artistId: Long,
        page: Int,
        size: Long
    ): Result<List<ArtistSingleData>, DataError.Network>
}