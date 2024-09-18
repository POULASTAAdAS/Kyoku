package com.poulastaa.core.domain.repository.work

import com.poulastaa.core.domain.model.AlbumWithSong
import com.poulastaa.core.domain.model.Artist
import com.poulastaa.core.domain.model.PlaylistWithSong
import com.poulastaa.core.domain.model.Song

interface LocalWorkDatasource {
    suspend fun getAllAlbumId(): List<Long>
    suspend fun saveAlbums(entry: List<AlbumWithSong>)
    suspend fun removeAlbums(list: List<Long>)

    suspend fun getAllPlaylistId(): List<Long>
    suspend fun savePlaylists(entry: List<PlaylistWithSong>)
    suspend fun removePlaylist(list: List<Long>)

    suspend fun getAllPlaylistSongsIdList(playlistId: Long): List<Long>
    suspend fun updatePlaylistsSongs(entry: PlaylistWithSong)
    suspend fun removePlaylistSongs(playlistId: Long, list: List<Long>)

    suspend fun getAllArtistsId(): List<Long>
    suspend fun saveArtists(entry: List<Artist>)
    suspend fun removeArtists(list: List<Long>)

    suspend fun getAllFavouriteId(): List<Long>
    suspend fun saveFavourite(entry: List<Song>)
    suspend fun removeFavourite(list: List<Long>)
}