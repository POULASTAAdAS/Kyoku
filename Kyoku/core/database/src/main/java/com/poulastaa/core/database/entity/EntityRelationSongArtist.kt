package com.poulastaa.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = [
        "songId",
        "artistId"
    ],
    foreignKeys = [
        ForeignKey(
            entity = EntityArtist::class,
            parentColumns = ["id"],
            childColumns = ["artistId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = EntitySong::class,
            parentColumns = ["id"],
            childColumns = ["songId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class EntityRelationSongArtist(
    @ColumnInfo(index = true)
    val songId: Long,
    @ColumnInfo(index = true)
    val artistId: Long,
)
