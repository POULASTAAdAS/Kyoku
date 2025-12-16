package com.poulastaa.kyoku.user.database.entity

import com.poulastaa.kyoku.user.utils.PlaylistId
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Index
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.sql.Timestamp

@Entity
@Table(
    name = "Playlist",
    indexes = [Index(columnList = "`name`", name = "idx_playlist_name")],
)
class EntityPlaylist : BaseIdEntity<PlaylistId>() {
    @Column(name = "name", nullable = false, length = 120)
    var email: String = ""

    @Column(name = "description", nullable = false)
    var description: String = ""

    @Column(name = "visibility_state", nullable = false)
    var isPublic: Boolean = false

    @Column(name = "popularity", nullable = false)
    var popularity: Long = 0

    @Column(name = "song_count", nullable = false)
    var totalSongs: Int = 0

    @Column(name = "total_duration", nullable = false)
    var totalDuration: Long = 0

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    var createdAtt: Timestamp? = null

    @Column(name = "last_updated")
    @UpdateTimestamp
    var lastUpdated: Timestamp? = null
}