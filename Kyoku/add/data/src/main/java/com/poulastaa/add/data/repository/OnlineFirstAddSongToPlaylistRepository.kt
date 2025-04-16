package com.poulastaa.add.data.repository

import androidx.paging.PagingData
import androidx.paging.filter
import com.poulastaa.add.domain.model.DtoAddSongToPlaylistItem
import com.poulastaa.add.domain.model.DtoAddSongToPlaylistPageItem
import com.poulastaa.add.domain.model.DtoAddSongToPlaylistSearchFilterType
import com.poulastaa.add.domain.model.DtoAddToPlaylistItemType
import com.poulastaa.add.domain.repository.AddSongToPlaylistRepository
import com.poulastaa.add.domain.repository.RemoteAddSongToPlaylistDatasource
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.EmptyResult
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.asEmptyDataResult
import com.poulastaa.core.domain.map
import com.poulastaa.core.domain.model.AlbumId
import com.poulastaa.core.domain.model.PlaylistId
import com.poulastaa.core.domain.model.SongId
import com.poulastaa.core.domain.repository.LocalAddSongToPlaylistDatasource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class OnlineFirstAddSongToPlaylistRepository @Inject constructor(
    private val remote: RemoteAddSongToPlaylistDatasource,
    private val local: LocalAddSongToPlaylistDatasource,
    private val scope: CoroutineScope,
) : AddSongToPlaylistRepository {
    override suspend fun loadStaticData(): Result<List<DtoAddSongToPlaylistPageItem>, DataError.Network> =
        remote.loadStaticData()

    override suspend fun search(
        playlistId: PlaylistId,
        query: String,
        filterType: DtoAddSongToPlaylistSearchFilterType,
    ): Flow<PagingData<DtoAddSongToPlaylistItem>> {
        val list = local.loadPlaylistSongIdList(playlistId).toSet()
        val pagingData = remote.search(query, filterType)

        return pagingData.map { data ->
            data.filter { item ->
                !(item.type == DtoAddToPlaylistItemType.SONG && list.contains(item.id))
            }
        }
    }

    override suspend fun saveSong(
        playlistId: PlaylistId,
        songId: SongId,
    ): EmptyResult<DataError.Network> {
        val result = remote.saveSong(playlistId, songId)
        if (result is Result.Success) scope.launch { local.saveSong(playlistId, result.data) }

        return result.asEmptyDataResult()
    }

    override suspend fun loadAlbum(
        playlistId: PlaylistId,
        albumId: AlbumId,
    ): Result<List<DtoAddSongToPlaylistItem>, DataError.Network> {
        val list = local.loadPlaylistSongIdList(playlistId).toSet()
        return remote.loadAlbum(albumId).map {
            it.filter { !list.contains(it.id) }
        }
    }
}