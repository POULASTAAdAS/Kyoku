package com.poulastaa.common.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.poulastaa.common.database.entity.PlaylistEntity
import com.poulastaa.common.database.entity.PlaylistSongEntity
import com.poulastaa.common.database.entity.PlaylistVisibilityEntity
import com.poulastaa.common.database.entity.relation.SongWithRelations
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PlaylistDao {
    @Upsert
    abstract suspend fun upsertPlaylist(playlist: PlaylistEntity)

    @Upsert
    abstract suspend fun upsertPlaylistSongs(links: List<PlaylistSongEntity>)

    @Query("DELETE FROM playlist_song WHERE playlistId = :playlistId")
    abstract suspend fun deletePlaylistSongs(playlistId: Long)

    @Query("DELETE FROM playlist_song WHERE playlistId = :playlistId AND songId = :songId")
    abstract suspend fun removeSong(playlistId: Long, songId: Long)

    @Query("SELECT COALESCE(MAX(position) + 1, 0) FROM playlist_song WHERE playlistId = :playlistId")
    abstract suspend fun getNextPosition(playlistId: Long): Int

    @Query("UPDATE playlist SET name = :name WHERE id = :playlistId")
    abstract suspend fun rename(playlistId: Long, name: String)

    @Query("UPDATE playlist SET visibility = :visibility WHERE id = :playlistId")
    abstract suspend fun updateVisibility(playlistId: Long, visibility: PlaylistVisibilityEntity)

    @Query("DELETE FROM playlist WHERE id = :playlistId")
    abstract suspend fun deletePlaylist(playlistId: Long)

    @Query("SELECT * FROM playlist ORDER BY name")
    abstract fun observePlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlist WHERE id = :playlistId")
    abstract fun observePlaylist(playlistId: Long): Flow<PlaylistEntity?>

    @Transaction
    @Query(
        """
        SELECT song.* FROM song
        INNER JOIN playlist_song ON playlist_song.songId = song.id
        WHERE playlist_song.playlistId = :playlistId
        ORDER BY playlist_song.position
        """
    )
    abstract fun observePlaylistSongs(playlistId: Long): Flow<List<SongWithRelations>>

    /** Replaces the playlist row and its song order. Songs must already be stored via [SongDao]. */
    @Transaction
    open suspend fun replacePlaylist(
        playlist: PlaylistEntity,
        songIds: List<Long>,
        addedAt: Long,
    ) {
        upsertPlaylist(playlist)
        deletePlaylistSongs(playlist.id)
        upsertPlaylistSongs(
            songIds.distinct().mapIndexed { index, songId ->
                PlaylistSongEntity(
                    playlistId = playlist.id,
                    songId = songId,
                    position = index,
                    addedAt = addedAt,
                )
            }
        )
    }

    @Transaction
    open suspend fun addSong(playlistId: Long, songId: Long, addedAt: Long) {
        upsertPlaylistSongs(
            listOf(
                PlaylistSongEntity(
                    playlistId = playlistId,
                    songId = songId,
                    position = getNextPosition(playlistId),
                    addedAt = addedAt,
                )
            )
        )
    }

    @Transaction
    open suspend fun reorder(playlistId: Long, songIds: List<Long>, addedAt: Long) {
        deletePlaylistSongs(playlistId)
        upsertPlaylistSongs(
            songIds.distinct().mapIndexed { index, songId ->
                PlaylistSongEntity(playlistId, songId, index, addedAt)
            }
        )
    }
}
