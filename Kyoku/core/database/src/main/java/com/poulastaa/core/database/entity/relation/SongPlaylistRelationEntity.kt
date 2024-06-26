package com.poulastaa.core.database.entity.relation

import androidx.room.Entity
import androidx.room.ForeignKey
import com.poulastaa.core.database.entity.PlaylistEntity
import com.poulastaa.core.database.entity.SonEntity

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = SonEntity::class,
            parentColumns = ["id"],
            childColumns = ["songId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PlaylistEntity::class,
            parentColumns = ["id"],
            childColumns = ["playlistId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    primaryKeys = ["songId", "playlistId"]
)
data class SongPlaylistRelationEntity(
    val songId: Long,
    val playlistId: Long,
)
