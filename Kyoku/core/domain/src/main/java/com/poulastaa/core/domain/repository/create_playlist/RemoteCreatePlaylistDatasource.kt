package com.poulastaa.core.domain.repository.create_playlist

import androidx.paging.PagingData
import com.poulastaa.core.domain.model.CreatePlaylistPagerFilterType
import com.poulastaa.core.domain.model.CreatePlaylistPagingData
import com.poulastaa.core.domain.model.CreatePlaylistType
import com.poulastaa.core.domain.model.Song
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow

interface RemoteCreatePlaylistDatasource {
    suspend fun getStaticData(): Result<List<Pair<CreatePlaylistType, List<Song>>>, DataError.Network>
    suspend fun getPagingSong(
        query: String,
        type: CreatePlaylistPagerFilterType,
        savedSongIdList: List<Long>,
    ): Flow<PagingData<CreatePlaylistPagingData>>

    suspend fun getSong(songId: Long): Result<Song, DataError.Network>
    suspend fun saveSong(songId: Long, playlistId: Long): EmptyResult<DataError.Network>
}