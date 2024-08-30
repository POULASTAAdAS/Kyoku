package com.poulastaa.core.domain.repository.work

import com.poulastaa.core.domain.model.AlbumWithSong
import com.poulastaa.core.domain.model.Artist
import com.poulastaa.core.domain.model.PlaylistWithSong

interface LocalWorkDatasource {
    suspend fun getAllAlbumId(): List<Long>
    suspend fun saveAlbums(entry: List<AlbumWithSong>)
    suspend fun removeAlbums(list: List<Long>)

    suspend fun getAllPlaylistId(): List<Long>
    suspend fun savePlaylists(entry: List<PlaylistWithSong>)
    suspend fun removePlaylist(list: List<Long>)

    suspend fun getAllArtistsId(): List<Long>
    suspend fun saveArtists(entry: List<Artist>)
    suspend fun removeArtists(list: List<Long>)
}