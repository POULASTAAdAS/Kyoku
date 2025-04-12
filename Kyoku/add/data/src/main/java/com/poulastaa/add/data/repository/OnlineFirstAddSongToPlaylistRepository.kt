package com.poulastaa.add.data.repository

import androidx.paging.PagingData
import com.poulastaa.add.domain.model.DtoAddSongToPlaylistItem
import com.poulastaa.add.domain.model.DtoAddSongToPlaylistPageItem
import com.poulastaa.add.domain.model.DtoAddSongToPlaylistSearchFilterType
import com.poulastaa.add.domain.repository.AddSongToPlaylistRepository
import com.poulastaa.add.domain.repository.RemoteAddSongToPlaylistDatasource
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.asEmptyDataResult
import com.poulastaa.core.domain.model.PlaylistId
import com.poulastaa.core.domain.model.SongId
import com.poulastaa.core.domain.repository.LocalAddSongToPlaylistDatasource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class OnlineFirstAddSongToPlaylistRepository @Inject constructor(
    private val remote: RemoteAddSongToPlaylistDatasource,
    private val local: LocalAddSongToPlaylistDatasource,
    private val scope: CoroutineScope,
) : AddSongToPlaylistRepository {
    override suspend fun loadStaticData(): Result<List<DtoAddSongToPlaylistPageItem>, DataError.Network> =
        remote.loadStaticData()

    override fun search(
        query: String,
        filterType: DtoAddSongToPlaylistSearchFilterType,
    ): Flow<PagingData<DtoAddSongToPlaylistItem>> = remote.search(query, filterType)

    override suspend fun saveSong(
        playlistId: PlaylistId,
        songId: SongId,
    ): Result<Unit, DataError.Network> {
        val result = remote.saveSong(playlistId, songId)
        if (result is Result.Success) scope.launch { local.saveSong(playlistId, result.data) }

        return result.asEmptyDataResult()
    }
}