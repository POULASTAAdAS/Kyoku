package com.poulastaa.core.domain.get_spotify_playlist

import com.poulastaa.core.domain.model.Playlist
import com.poulastaa.core.domain.model.PlaylistWithSongInfo
import com.poulastaa.core.domain.model.Song
import com.poulastaa.core.domain.model.SongColor
import kotlinx.coroutines.flow.Flow

typealias songId = Long
typealias playlistId = Long

interface LocalSpotifyDataSource {
    suspend fun insertSongs(songs: List<Song>): List<songId>
    suspend fun insertPlaylist(playlist: Playlist): playlistId

    suspend fun createRelationOnSongAndPlaylist(songIdList: List<songId>, playlistId: playlistId)

    suspend fun getSongOnUrl(url: String): songId?

    suspend fun getSongColorInfo(songId: Long): SongColor?

    suspend fun addColorToSong(
        songId: songId,
        primary: String,
        background: String,
        onBackground: String,
    )

    fun getAllPlaylistWithSong(): Flow<List<PlaylistWithSongInfo>>
}