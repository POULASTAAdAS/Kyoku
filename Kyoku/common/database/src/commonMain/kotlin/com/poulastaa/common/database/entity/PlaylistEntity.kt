package com.poulastaa.common.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist")
data class PlaylistEntity(
    @PrimaryKey
    val id: Long,
    val name: String,
    val popularity: Long,
    val visibility: PlaylistVisibilityEntity,
    /** Source spotify playlist id; `null` when not imported from spotify. */
    val spotifyPlaylistId: String? = null,
)

enum class PlaylistVisibilityEntity {
    PUBLIC,
    PRIVATE,
}
