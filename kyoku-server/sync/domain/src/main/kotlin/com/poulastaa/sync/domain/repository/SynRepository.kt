package com.poulastaa.sync.domain.repository

import com.poulastaa.core.domain.model.DtoSyncPayload
import com.poulastaa.core.domain.model.DtoSyncPlaylistSongPayload
import com.poulastaa.core.domain.model.DtoSyncType
import com.poulastaa.core.domain.model.ReqUserPayload
import com.poulastaa.core.domain.repository.PlaylistId
import com.poulastaa.core.domain.repository.SongId

interface SynRepository {
    suspend fun <T> syncData(type: DtoSyncType, savedIdList: List<Long>, payload: ReqUserPayload): DtoSyncPayload<T>?
    suspend fun syncPlaylistSongs(
        idList: List<Pair<PlaylistId, List<SongId>>>,
        payload: ReqUserPayload,
    ): DtoSyncPlaylistSongPayload?
}