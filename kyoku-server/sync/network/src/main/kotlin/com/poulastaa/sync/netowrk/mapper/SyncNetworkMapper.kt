package com.poulastaa.sync.netowrk.mapper

import com.poulastaa.core.domain.model.*
import com.poulastaa.core.network.mapper.toResponseArtist
import com.poulastaa.core.network.mapper.toResponseFullAlbum
import com.poulastaa.core.network.mapper.toResponsePlaylistFull
import com.poulastaa.core.network.mapper.toResponseSong
import com.poulastaa.core.network.model.ResponseArtist
import com.poulastaa.core.network.model.ResponseFullAlbum
import com.poulastaa.core.network.model.ResponseFullPlaylist
import com.poulastaa.core.network.model.ResponseSong
import com.poulastaa.sync.netowrk.model.SyncPlaylistSongResponse
import com.poulastaa.sync.netowrk.model.SyncResponse
import com.poulastaa.sync.netowrk.model.SyncType

internal fun DtoSyncPlaylistSongPayload.toResponseSyncPlaylistSong() = SyncPlaylistSongResponse(
    removeIdList = this.removeIdList,
    newData = newData.map {
        it.first to it.second.map { it.toResponseSong() }
    }
)

internal fun SyncType.toDtoSyncType() = when (this) {
    SyncType.SYNC_ALBUM -> DtoSyncType.SYNC_ALBUM
    SyncType.SYNC_PLAYLIST -> DtoSyncType.SYNC_PLAYLIST
    SyncType.SYNC_ARTIST -> DtoSyncType.SYNC_ARTIST
    SyncType.SYNC_FAVOURITE -> DtoSyncType.SYNC_FAVOURITE
    else -> throw Exception("Unsupported Sync type: $this")
}

@JvmName("toResponseAlbum")
internal fun DtoSyncPayload<DtoFullAlbum>.toDtoSyncData() = SyncResponse<ResponseFullAlbum>(
    removeIdList = this.removeIdList,
    newData = this.newData.map { it.toResponseFullAlbum() }
)

@JvmName("toResponsePlaylist")
internal fun DtoSyncPayload<DtoFullPlaylist>.toDtoSyncData() = SyncResponse<ResponseFullPlaylist>(
    removeIdList = this.removeIdList,
    newData = this.newData.map { it.toResponsePlaylistFull() }
)

@JvmName("toResponseArtist")
internal fun DtoSyncPayload<DtoArtist>.toDtoSyncData() = SyncResponse<ResponseArtist>(
    removeIdList = this.removeIdList,
    newData = this.newData.map { it.toResponseArtist() }
)

@JvmName("toResponseFavouriteSongs")
internal fun DtoSyncPayload<DtoSong>.toDtoSyncData() = SyncResponse<ResponseSong>(
    removeIdList = this.removeIdList,
    newData = this.newData.map { it.toResponseSong() }
)