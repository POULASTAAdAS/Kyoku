package com.poulastaa.sync.repository

import com.poulastaa.core.domain.model.DtoSyncPayload
import com.poulastaa.core.domain.model.DtoSyncPlaylistSongPayload
import com.poulastaa.core.domain.model.DtoSyncType
import com.poulastaa.core.domain.repository.PlaylistId
import com.poulastaa.core.domain.repository.SongId
import com.poulastaa.core.domain.repository.sync.LocalSyncCacheDatasource
import com.poulastaa.core.domain.repository.sync.LocalSyncDatasource
import com.poulastaa.sync.domain.repository.SynRepository

internal class SyncRepositoryService(
    private val local: LocalSyncDatasource,
    private val cache: LocalSyncCacheDatasource,
) : SynRepository {
    override suspend fun <T> syncData(
        type: DtoSyncType,
        idList: List<Long>,
    ): DtoSyncPayload<T> {
        when(type){
            DtoSyncType.SYNC_ALBUM -> TODO()
            DtoSyncType.SYNC_PLAYLIST -> TODO()
            DtoSyncType.SYNC_ARTIST -> TODO()
            DtoSyncType.SYNC_FAVOURITE -> TODO()
        }
    }

    override suspend fun syncPlaylistSongs(idList: List<Pair<PlaylistId, List<SongId>>>): DtoSyncPlaylistSongPayload {
        TODO("Not yet implemented")
    }
}