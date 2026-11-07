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
)

enum class PlaylistVisibilityEntity {
    PUBLIC,
    PRIVATE,
}
