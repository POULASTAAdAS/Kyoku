package com.poulastaa.kyoku.activity.database.playlist.entity

import com.poulastaa.kyoku.activity.database.BaseIdEntity
import com.poulastaa.kyoku.activity.utils.PlaylistId
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
    indexes = [Index(columnList = "`name`", name = "idx_playlist_name")],
)
class EntityPlaylist(
    @Column(name = "name", nullable = false, length = 120)
    var name: String = "",

    @Column(name = "description", nullable = false)
    var description: String = "",

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