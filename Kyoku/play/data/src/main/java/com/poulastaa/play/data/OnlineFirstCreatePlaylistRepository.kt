package com.poulastaa.play.data

import androidx.paging.PagingData
import com.poulastaa.core.domain.model.CreatePlaylistPagerFilterType
import com.poulastaa.core.domain.model.CreatePlaylistPagingData
import com.poulastaa.core.domain.model.CreatePlaylistType
import com.poulastaa.core.domain.model.Song
import com.poulastaa.core.domain.repository.create_playlist.CreatePlaylistRepository
import com.poulastaa.core.domain.repository.create_playlist.LocalCreatePlaylistDatasource
import com.poulastaa.core.domain.repository.create_playlist.RemoteCreatePlaylistDatasource
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.domain.utils.asEmptyDataResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OnlineFirstCreatePlaylistRepository @Inject constructor(
    private val local: LocalCreatePlaylistDatasource,
    private val remote: RemoteCreatePlaylistDatasource,
    private val applicationScope: CoroutineScope
) : CreatePlaylistRepository {
    override suspend fun getStaticData(): Result<List<Pair<CreatePlaylistType, List<Song>>>, DataError.Network> =
        remote.getStaticData()

    override suspend fun getPagingSong(
        query: String,
        type: CreatePlaylistPagerFilterType,
        savedSongIdList: List<Long>
    ): Flow<PagingData<CreatePlaylistPagingData>> =
        remote.getPagingSong(query, type, savedSongIdList)

    override suspend fun saveSong(song: Song, playlistId: Long): EmptyResult<DataError.Network> {
        val result = remote.saveSong(songId = song.id, playlistId = playlistId)
        if (result is Result.Success) applicationScope.async { local.saveSong(song, playlistId) }
            .await()

        return result.asEmptyDataResult()
    }

    override suspend fun saveSong(songId: Long, playlistId: Long): EmptyResult<DataError.Network> {
        val result = remote.getSong(songId)
        if (result is Result.Success) applicationScope.async { local.saveSong(result.data, playlistId) }
            .await()

        return result.asEmptyDataResult()
    }
}