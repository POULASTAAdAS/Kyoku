package com.poulastaa.kyoku.playlist.database.playlist.entity

import com.poulastaa.kyoku.playlist.database.BaseIdEntity
import com.poulastaa.kyoku.playlist.utils.PlaylistId
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Index
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.sql.Timestamp

enum class PlaylistVisibility(val status: Boolean) {
    PUBLIC(true),
    PRIVATE(false)
}

@Entity
@Table(
    name = "Playlist",
    indexes = [
        Index(columnList = "`name`", name = "idx_playlist_name"),
        Index(columnList = "spotify_playlist_id", name = "idx_playlist_spotify_playlist_id"),
    ],
)
class EntityPlaylist(
    @Column(name = "name", nullable = false, length = 120)
    var name: String = "",

    @Column(name = "description", nullable = false)
    var description: String = "",

    @Column(name = "spotify_playlist_id", length = 64)
    var spotifyPlaylistId: String? = null,

    @Column(name = "visibility_state", nullable = false)
    var visibility: Boolean = PlaylistVisibility.PRIVATE.status,

    @Column(name = "popularity", nullable = false)
    var popularity: Long = 0,

    @Column(name = "song_count", nullable = false)
    var totalSongs: Int = 0,

    @Column(name = "total_duration", nullable = false)
    var totalDuration: Long = 0,
) : BaseIdEntity<PlaylistId>() {
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    var createdAtt: Timestamp? = null

    @Column(name = "last_updated")
    @UpdateTimestamp
    var lastUpdated: Timestamp? = null
}