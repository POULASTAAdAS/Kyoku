package com.poulastaa.core.domain.get_spotify_playlist

import com.poulastaa.core.domain.model.Playlist
import com.poulastaa.core.domain.model.PlaylistWithSongInfo
import com.poulastaa.core.domain.model.Song
import kotlinx.coroutines.flow.Flow

typealias songId = Long
typealias playlistId = Long

interface LocalSpotifyDataSource {
    suspend fun insertSongs(songs: List<Song>): List<songId>
    suspend fun insertPlaylist(playlist: Playlist): playlistId

    suspend fun createRelationOnSongAndPlaylist(songIdList: List<songId>, playlistId: playlistId)

    suspend fun getSongOnUrl(url: String): songId?

    fun getAllPlaylistWithSong(): Flow<List<PlaylistWithSongInfo>>
}