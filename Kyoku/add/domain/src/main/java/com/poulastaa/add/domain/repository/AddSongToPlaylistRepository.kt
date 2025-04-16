package com.poulastaa.add.domain.repository

import androidx.paging.PagingData
import com.poulastaa.add.domain.model.DtoAddSongToPlaylistItem
import com.poulastaa.add.domain.model.DtoAddSongToPlaylistPageItem
import com.poulastaa.add.domain.model.DtoAddSongToPlaylistSearchFilterType
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.EmptyResult
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.model.AlbumId
import com.poulastaa.core.domain.model.PlaylistId
import com.poulastaa.core.domain.model.SongId
import kotlinx.coroutines.flow.Flow

interface AddSongToPlaylistRepository {
    suspend fun loadStaticData(): Result<List<DtoAddSongToPlaylistPageItem>, DataError.Network>
    suspend fun search(
        playlistId: PlaylistId,
        query: String,
        filterType: DtoAddSongToPlaylistSearchFilterType,
    ): Flow<PagingData<DtoAddSongToPlaylistItem>>

    suspend fun saveSong(
        playlistId: PlaylistId,
        songId: SongId,
    ): EmptyResult<DataError.Network>

    suspend fun loadAlbum(
        playlistId: PlaylistId,
        albumId: AlbumId,
    ): Result<List<DtoAddSongToPlaylistItem>, DataError.Network>
}