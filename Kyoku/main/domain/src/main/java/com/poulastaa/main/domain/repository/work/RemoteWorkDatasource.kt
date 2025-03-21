package com.poulastaa.main.domain.repository.work

import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.model.AlbumId
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.DtoArtist
import com.poulastaa.core.domain.model.DtoFullAlbum
import com.poulastaa.core.domain.model.DtoFullPlaylist
import com.poulastaa.core.domain.model.DtoSong
import com.poulastaa.core.domain.model.PlaylistId
import com.poulastaa.core.domain.model.SongId
import com.poulastaa.main.domain.model.DtoSyncData
import com.poulastaa.main.domain.model.DtoSyncPlaylistSongs

interface RemoteWorkDatasource {
    suspend fun syncSavedAlbums(list: List<AlbumId>): Result<DtoSyncData<DtoFullAlbum>, DataError.Network>
    suspend fun syncSavedPlaylist(list: List<PlaylistId>): Result<DtoSyncData<DtoFullPlaylist>, DataError.Network>
    suspend fun syncSavedArtist(list: List<ArtistId>): Result<DtoSyncData<DtoArtist>, DataError.Network>
    suspend fun syncSavedFavourite(list: List<SongId>): Result<DtoSyncData<DtoSong>, DataError.Network>

    suspend fun syncSavedPlaylistSongs(list: List<Pair<PlaylistId, List<SongId>>>): Result<DtoSyncPlaylistSongs<Pair<PlaylistId, List<DtoSong>>>, DataError.Network>
}