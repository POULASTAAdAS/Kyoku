package com.poulastaa.data.repository.playlist

import com.poulastaa.data.model.utils.PlaylistRow
import com.poulastaa.domain.dao.playlist.EmailUserPlaylist
import com.poulastaa.domain.dao.playlist.GoogleUserPlaylist
import com.poulastaa.domain.dao.playlist.PasskeyUserPlaylist
import com.poulastaa.domain.dao.playlist.Playlist
import com.poulastaa.domain.repository.playlist.PlaylistRepository
import com.poulastaa.plugins.dbQuery

class PlaylistRepositoryImpl : PlaylistRepository {
    override suspend fun cretePlaylistForEmailUser(list: List<PlaylistRow>, playlist: Playlist) {
        list.forEach {
            dbQuery {
                EmailUserPlaylist.new {
                    this.playlistId = playlist.id.value
                    this.songId = it.songId
                    this.userId = it.userId
                }
            }
        }
    }

    override suspend fun cretePlaylistForGoogleUser(list: List<PlaylistRow>, playlist: Playlist) {
        list.forEach {
            dbQuery {
                GoogleUserPlaylist.new {
                    this.playlistId = playlist.id.value
                    this.songId = it.songId
                    this.userId = it.userId
                }
            }
        }
    }

    override suspend fun cretePlaylistForPasskeyUser(list: List<PlaylistRow>, playlist: Playlist) {
        list.forEach {
            dbQuery {
                PasskeyUserPlaylist.new {
                    this.playlistId = playlist.id.value
                    this.songId = it.songId
                    this.userId = it.userId
                }
            }
        }
    }

    override fun getPlaylist() {

    }
}