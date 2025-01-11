package com.poulastaa.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = [
        "playlistName",
        "songId"
    ],
    foreignKeys = [
        ForeignKey(
            entity = EntitySong::class,
            parentColumns = ["id"],
            childColumns = ["songId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class EntityRelationSuggestedPlaylist(
    @ColumnInfo(index = true)
    val playlistName: String,
    @ColumnInfo(index = true)
    val songId: Long,
)
