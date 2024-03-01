package com.poulastaa.data.repository.playlist

import com.poulastaa.data.model.utils.PlaylistRow
import com.poulastaa.domain.dao.playlist.EmailUserPlaylist
import com.poulastaa.domain.dao.playlist.GoogleUserPlaylist
import com.poulastaa.domain.dao.playlist.PasskeyUserPlaylist
import com.poulastaa.domain.dao.playlist.Playlist
import com.poulastaa.domain.repository.playlist.PlaylistRepository
import com.poulastaa.plugins.dbQuery

class PlaylistRepositoryImpl : PlaylistRepository {
    private suspend fun createPlaylist(name: String) = dbQuery {
        Playlist.new {
            this.name = name
        }
    }.id.value

    override suspend fun cretePlaylistForEmailUser(playlist: List<PlaylistRow>, playlistName: String) {
        val playlistId = createPlaylist(playlistName)

        playlist.forEach {
            dbQuery {
                EmailUserPlaylist.new {
                    this.playlistId = playlistId
                    this.songId = it.songId
                    this.userId = it.userId
                }
            }
        }
    }

    override suspend fun cretePlaylistForGoogleUser(playlist: List<PlaylistRow>, playlistName: String) {
        val playlistId = createPlaylist(playlistName)

        playlist.forEach {
            dbQuery {
                GoogleUserPlaylist.new {
                    this.playlistId = playlistId
                    this.songId = it.songId
                    this.userId = it.userId
                }
            }
        }
    }

    override suspend fun cretePlaylistForPasskeyUser(playlist: List<PlaylistRow>, playlistName: String) {
        val playlistId = createPlaylist(playlistName)

        playlist.forEach {
            dbQuery {

                PasskeyUserPlaylist.new {
                    this.playlistId = playlistId
                    this.songId = it.songId
                    this.userId = it.userId
                }
            }
        }
    }

    override fun getPlaylist() {

    }
}