package com.poulastaa.data.repository.playlist

import com.poulastaa.data.model.PlaylistRow
import com.poulastaa.domain.dao.playlist.EmailUserPlaylist
import com.poulastaa.domain.dao.playlist.GoogleUserPlaylist
import com.poulastaa.domain.dao.playlist.PasskeyUserPlaylist
import com.poulastaa.domain.repository.playlist.PlaylistRepository
import com.poulastaa.plugins.dbQuery

class PlaylistRepositoryImpl : PlaylistRepository {
    override suspend fun cretePlaylistForEmailUser(playlist: List<PlaylistRow>) {
        try {
            playlist.forEach {
                dbQuery {
                    EmailUserPlaylist.new {
                        this.songId = it.songId
                        this.userEmail = it.userId
                    }
                }
            }
        } catch (_: Exception) {
        }
    }

    override suspend fun cretePlaylistForGoogleUser(playlist: List<PlaylistRow>) {
        try {
            playlist.forEach {
                dbQuery {
                    GoogleUserPlaylist.new {
                        this.songId = it.songId
                        this.userId = it.userId
                    }
                }
            }
        } catch (_: Exception) {
        }
    }

    override suspend fun cretePlaylistForPasskeyUser(playlist: List<PlaylistRow>) {
        try {
            playlist.forEach {
                PasskeyUserPlaylist.new {
                    this.songId = it.songId
                    this.userId = it.userId
                }
            }
        } catch (_: Exception) {
        }
    }

    override fun getPlaylist() {

    }
}