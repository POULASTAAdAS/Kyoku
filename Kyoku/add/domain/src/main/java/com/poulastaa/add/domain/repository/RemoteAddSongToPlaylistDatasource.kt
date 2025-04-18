package com.poulastaa.add.domain.repository

import androidx.paging.PagingData
import com.poulastaa.add.domain.model.DtoAddSongToPlaylistArtistSearchFilterType
import com.poulastaa.add.domain.model.DtoAddSongToPlaylistItem
import com.poulastaa.add.domain.model.DtoAddSongToPlaylistPageItem
import com.poulastaa.add.domain.model.DtoAddSongToPlaylistSearchFilterType
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.model.AlbumId
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.core.domain.model.DtoSong
import com.poulastaa.core.domain.model.PlaylistId
import com.poulastaa.core.domain.model.SongId
import kotlinx.coroutines.flow.Flow

interface RemoteAddSongToPlaylistDatasource {
    suspend fun loadStaticData(): Result<List<DtoAddSongToPlaylistPageItem>, DataError.Network>
    fun search(
        query: String,
        filterType: DtoAddSongToPlaylistSearchFilterType,
    ): Flow<PagingData<DtoAddSongToPlaylistItem>>

    suspend fun saveSong(
        playlistId: PlaylistId,
        songId: SongId,
    ): Result<DtoSong, DataError.Network>

    suspend fun loadAlbum(albumId: AlbumId): Result<Pair<String, List<DtoAddSongToPlaylistItem>>, DataError.Network>

    suspend fun loadArtist(artistId: ArtistId): Result<DtoPrevArtist, DataError.Network>

    suspend fun loadArtistSearch(
        artistId: ArtistId,
        query: String,
        filterType: DtoAddSongToPlaylistArtistSearchFilterType,
    ): Flow<PagingData<DtoAddSongToPlaylistItem>>

}