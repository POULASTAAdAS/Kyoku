package com.poulastaa.core.domain.repository.work

import com.poulastaa.core.domain.model.AlbumWithSong
import com.poulastaa.core.domain.model.Artist
import com.poulastaa.core.domain.model.PlaylistWithSong
import com.poulastaa.core.domain.model.SyncData
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result

interface RemoteWorkDatasource {
    suspend fun getUpdatedAlbums(list: List<Long>): Result<SyncData<AlbumWithSong>, DataError.Network>
    suspend fun getUpdatedPlaylists(list: List<Long>): Result<SyncData<PlaylistWithSong>, DataError.Network>
    suspend fun getUpdatedArtists(list: List<Long>): Result<SyncData<Artist>, DataError.Network>
}