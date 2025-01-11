package com.poulastaa.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = [
        "playlistId",
        "songId"
    ],
    foreignKeys = [
        ForeignKey(
            entity = EntitySong::class,
            parentColumns = ["id"],
            childColumns = ["songId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = EntityPlaylist::class,
            parentColumns = ["id"],
            childColumns = ["playlistId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class EntityRelationSongPlaylist(
    @ColumnInfo(index = true)
    val songId: Long,
    @ColumnInfo(index = true)
    val playlistId: Long,
)
