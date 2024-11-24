package com.poulastaa.core.domain.repository.add_to_playlist

import com.poulastaa.core.domain.model.PlaylistData
import com.poulastaa.core.domain.model.Song
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.Result

interface RemoteAddToPlaylistDatasource {
    suspend fun saveSong(songId: Long): Result<Song, DataError.Network>
    suspend fun editPlaylist(
        songId: Long,
        playlistIdList: Map<Long, Boolean>,
    ): EmptyResult<DataError.Network>

    suspend fun addSongToFavourite(songId: Long): EmptyResult<DataError.Network>
    suspend fun removeSongToFavourite(songId: Long): EmptyResult<DataError.Network>

    suspend fun createPlaylist(
        songId: Long,
        name: String,
    ): Result<PlaylistData, DataError.Network>
}