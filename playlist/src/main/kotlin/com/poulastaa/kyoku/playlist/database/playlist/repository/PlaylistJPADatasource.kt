package com.poulastaa.kyoku.playlist.database.playlist.repository

import com.poulastaa.kyoku.playlist.database.playlist.entity.EntityPlaylist
import com.poulastaa.kyoku.playlist.database.playlist.entity.EntitySongPlaylist
import com.poulastaa.kyoku.playlist.database.playlist.entity.EntityUserPlaylist
import com.poulastaa.kyoku.playlist.database.playlist.entity.ids.SongPlaylistId
import com.poulastaa.kyoku.playlist.database.playlist.entity.ids.UserPlaylistId
import com.poulastaa.kyoku.playlist.utils.PlaylistId
import com.poulastaa.kyoku.playlist.utils.SpotifyPlaylistId
import com.poulastaa.kyoku.playlist.utils.UserId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface PlaylistDataSource : JpaRepository<EntityPlaylist, PlaylistId>
interface SongPlaylistDataSource : JpaRepository<EntitySongPlaylist, SongPlaylistId> {
    @Query("SELECT sp FROM EntitySongPlaylist sp WHERE sp.id.playlistId IN :playlistIds")
    fun findAllByPlaylistIds(@Param("playlistIds") playlistIds: List<PlaylistId>): List<EntitySongPlaylist>
}

interface UserPlaylistDataSource : JpaRepository<EntityUserPlaylist, UserPlaylistId> {
    @Query(
        """
            SELECT COUNT(up) > 0 FROM EntityUserPlaylist up
            WHERE up.id.userId = :userId
            AND up.playlist.spotifyPlaylistId = :spotifyPlaylistId
        """
    )
    fun existsByUserIdAndSpotifyPlaylistId(
        @Param("userId") userId: UserId,
        @Param("spotifyPlaylistId") spotifyPlaylistId: SpotifyPlaylistId,
    ): Boolean

    @Query("SELECT up.playlist FROM EntityUserPlaylist up WHERE up.id.userId = :userId")
    fun findPlaylistsByUserId(@Param("userId") userId: UserId): List<EntityPlaylist>
}
