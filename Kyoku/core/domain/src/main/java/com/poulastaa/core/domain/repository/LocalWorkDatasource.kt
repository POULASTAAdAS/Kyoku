package com.poulastaa.core.domain.repository

import com.poulastaa.core.domain.model.AlbumId
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.DtoArtist
import com.poulastaa.core.domain.model.DtoFullAlbum
import com.poulastaa.core.domain.model.DtoFullPlaylist
import com.poulastaa.core.domain.model.DtoSong
import com.poulastaa.core.domain.model.PlaylistId
import com.poulastaa.core.domain.model.SongId

interface LocalWorkDatasource {
    suspend fun getSavedAlbumIds(): List<AlbumId>
    suspend fun removeSavedAlbums(list: List<AlbumId>)
    suspend fun saveAlbums(list: List<DtoFullAlbum>)

    suspend fun getSavedPlaylistIds(): List<PlaylistId>
    suspend fun removeSavedPlaylists(list: List<PlaylistId>)
    suspend fun savePlaylists(list: List<DtoFullPlaylist>)

    suspend fun getSavedArtistIds(): List<ArtistId>
    suspend fun removeSavedArtists(list: List<ArtistId>)
    suspend fun saveArtists(list: List<DtoArtist>)

    suspend fun getSavedFavouriteIds(): List<SongId>
    suspend fun removeSavedFavourites(list: List<SongId>)
    suspend fun saveFavourites(list: List<DtoSong>)
}