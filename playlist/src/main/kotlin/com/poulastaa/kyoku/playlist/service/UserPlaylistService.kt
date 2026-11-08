package com.poulastaa.kyoku.playlist.service

import com.poulastaa.kyoku.grpc.model.RequestUser
import org.springframework.stereotype.Service

/**
 * Read flow: every playlist of the user with its full songs.
 */
@Service
class UserPlaylistService(
    private val userClient: UserClient,
    private val playlistRepo: PlaylistRepository,
    private val writer: PlaylistWriter,
) {
    fun getPlaylists(user: RequestUser): List<ImportedPlaylist> {
        val userId = userClient.getUserId(user)
        val playlists = writer.getUserPlaylists(userId)
        if (playlists.isEmpty()) return emptyList()

        // one song lookup for all playlists: cache first, CONTENT DB for the misses
        val songsById = playlistRepo.getSongsByIds(playlists.flatMap { it.second }.distinct()).associateBy { it.id }

        return playlists.map { (playlist, songIds) ->
            ImportedPlaylist(playlist = playlist, songs = songIds.mapNotNull { songsById[it] })
        }
    }
}
