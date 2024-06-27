package com.poulastaa.core.domain.get_spotify_playlist

import kotlinx.coroutines.flow.Flow

typealias songId = Long
typealias playlistId = Long

interface LocalSpotifyDataSource {
    suspend fun insertSongs(songs: List<Song>): List<songId>
    suspend fun insertPlaylist(playlist: Playlist): playlistId

    suspend fun addColorToSong(songId: songId, encodedCoverImage: String)

    suspend fun getAllPlaylistWithSong(): Flow<List<PlaylistWithSong>>
}