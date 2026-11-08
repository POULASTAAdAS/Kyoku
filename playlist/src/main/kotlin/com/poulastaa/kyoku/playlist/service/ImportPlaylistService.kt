package com.poulastaa.kyoku.playlist.service

import com.poulastaa.kyoku.grpc.model.RequestUser
import com.poulastaa.kyoku.playlist.database.playlist.entity.EntityPlaylist
import com.poulastaa.kyoku.playlist.domain.model.DtoSong
import com.poulastaa.kyoku.playlist.domain.model.ImportPlaylistException
import com.poulastaa.kyoku.playlist.utils.DebugUtils
import io.grpc.Status
import org.springframework.stereotype.Service

data class ImportedPlaylist(
    val playlist: EntityPlaylist,
    val songs: List<DtoSong>,
)

/**
 * Import flow: every remote / slow step runs before the single local write transaction,
 * so a failure at any point leaves nothing behind.
 */
@Service
class ImportPlaylistService(
    private val userClient: UserClient,
    private val spotify: SpotifyClient,
    private val songMatcher: SongMatcher,
    private val playlistRepo: PlaylistRepository,
    private val writer: PlaylistWriter,
) {
    /**
     * @param force re-import even when [user] already imported [spotifyPlaylistId]; a new copy is created.
     */
    fun import(spotifyPlaylistId: String, user: RequestUser, force: Boolean): ImportedPlaylist {
        val userId = userClient.getUserId(user)

        // checked before any remote / slow step so the warning returns fast
        if (!force && writer.isSpotifyPlaylistImported(userId, spotifyPlaylistId)) throw ImportPlaylistException(
            Status.ALREADY_EXISTS, "Playlist already imported"
        )

        val spotifyPlaylist = spotify.getPlaylist(spotifyPlaylistId)
        if (spotifyPlaylist.tracks.isEmpty()) throw ImportPlaylistException(
            Status.NOT_FOUND, "Spotify playlist is empty"
        )

        val songIds = songMatcher.match(spotifyPlaylist.tracks)
        val songs = playlistRepo.getSongsByIds(songIds)
        if (songs.isEmpty()) throw ImportPlaylistException(
            Status.FAILED_PRECONDITION, "No matching songs found"
        )

        DebugUtils.d("importing spotify playlist $spotifyPlaylistId: ${songs.size}/${spotifyPlaylist.tracks.size} tracks matched")

        val playlist = writer.saveImportedPlaylist(
            userId = userId,
            spotifyPlaylistId = spotifyPlaylistId,
            name = spotifyPlaylist.name,
            songIds = songs.map { it.id }
        )

        return ImportedPlaylist(playlist = playlist, songs = songs)
    }
}
