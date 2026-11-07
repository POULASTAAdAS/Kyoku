package com.poulastaa.kyoku.playlist.service

import com.poulastaa.kyoku.playlist.database.playlist.entity.EntityPlaylist
import com.poulastaa.kyoku.playlist.database.playlist.entity.EntitySongPlaylist
import com.poulastaa.kyoku.playlist.database.playlist.entity.EntityUserPlaylist
import com.poulastaa.kyoku.playlist.database.playlist.entity.PlaylistVisibility
import com.poulastaa.kyoku.playlist.database.playlist.entity.ids.SongPlaylistId
import com.poulastaa.kyoku.playlist.database.playlist.entity.ids.UserPlaylistId
import com.poulastaa.kyoku.playlist.database.playlist.repository.PlaylistDataSource
import com.poulastaa.kyoku.playlist.database.playlist.repository.SongPlaylistDataSource
import com.poulastaa.kyoku.playlist.database.playlist.repository.UserPlaylistDataSource
import com.poulastaa.kyoku.playlist.utils.SongId
import com.poulastaa.kyoku.playlist.utils.UserId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private const val DEFAULT_PLAYLIST_NAME_PREFIX = "Playlist #"
private const val IMPORTED_PLAYLIST_DESCRIPTION = "Enjoy your imported playlist"
private const val PLAYLIST_NAME_MAX_LENGTH = 120

@Service
class PlaylistWriter(
    private val playlistDB: PlaylistDataSource,
    private val songPlaylistDB: SongPlaylistDataSource,
    private val userPlaylistDB: UserPlaylistDataSource,
) {
    /**
     * Saves the playlist, its songs and the user link in **one** PLAYLIST DB transaction.
     *
     * Default name `Playlist #N`: N is the playlist's own auto-increment id, assigned by MySQL inside this
     * transaction. Ids are unique even under concurrent imports, so two parallel inserts can never get the
     * same name and no lock / `count()` read is needed. (N may skip numbers; uniqueness is what is guaranteed.)
     */
    @Transactional(transactionManager = "playlistTransactionManger")
    fun saveImportedPlaylist(
        userId: UserId,
        name: String?,
        songIds: List<SongId>,
    ): EntityPlaylist {
        val playlistName = name?.trim()?.take(PLAYLIST_NAME_MAX_LENGTH)?.takeIf { it.isNotEmpty() }

        val playlist = playlistDB.saveAndFlush( // flush → id assigned by IDENTITY insert
            EntityPlaylist(
                name = playlistName ?: DEFAULT_PLAYLIST_NAME_PREFIX,
                description = IMPORTED_PLAYLIST_DESCRIPTION,
                visibility = PlaylistVisibility.PRIVATE.status,
                totalSongs = songIds.size,
                totalDuration = 0, // TODO add when song duration is available in CONTENT DB
            )
        )

        if (playlistName == null) playlist.name =
            "$DEFAULT_PLAYLIST_NAME_PREFIX${playlist.id}" // dirty-checked on commit

        songPlaylistDB.saveAll(
            songIds.map { songId ->
                EntitySongPlaylist(
                    id = SongPlaylistId(songId = songId, playlistId = playlist.id),
                    playlist = playlist
                )
            }
        )

        userPlaylistDB.save(
            EntityUserPlaylist(
                id = UserPlaylistId(userId = userId, playlistId = playlist.id),
                playlist = playlist
            )
        )

        return playlist
    }
}
