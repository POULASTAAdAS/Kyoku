package com.poulastaa.core.domain.repository.create_playlist.artist

import androidx.paging.PagingData
import com.poulastaa.core.domain.model.Artist
import com.poulastaa.core.domain.model.CreatePlaylistPagingData
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow

interface RemoteCreatePlaylistArtistDatasource {
    suspend fun getArtist(artistId: Long): Result<Artist, DataError.Network>
    suspend fun getPagingAlbum(
        artistId: Long,
    ): Flow<PagingData<CreatePlaylistPagingData>>

    suspend fun getPagingSong(
        artistId: Long,
        savedSongIdList: List<Long>
    ): Flow<PagingData<CreatePlaylistPagingData>>
}