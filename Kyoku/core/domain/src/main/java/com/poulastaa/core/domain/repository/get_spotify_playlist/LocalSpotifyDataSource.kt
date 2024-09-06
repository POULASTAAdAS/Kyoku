package com.poulastaa.core.domain.repository.get_spotify_playlist

import com.poulastaa.core.domain.model.Playlist
import com.poulastaa.core.domain.model.PlaylistWithSongInfo
import com.poulastaa.core.domain.model.Song
import kotlinx.coroutines.flow.Flow

typealias SongId = Long
typealias playlistId = Long

interface LocalSpotifyDataSource {
    suspend fun insertSongs(songs: List<Song>): List<SongId>
    suspend fun insertPlaylist(playlist: Playlist): playlistId

    suspend fun createRelationOnSongAndPlaylist(songIdList: List<SongId>, playlistId: playlistId)

    suspend fun getSongOnUrl(url: String): SongId?

    fun getAllPlaylistWithSong(): Flow<List<PlaylistWithSongInfo>>
}