package com.poulastaa.kyoku.data.model.database.table

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "SongAlbumRelationTable",
    foreignKeys = [
        ForeignKey(
            entity = AlbumSongTable::class,
            parentColumns = ["id"],
            childColumns = ["songId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = AlbumTable::class,
            parentColumns = ["id"],
            childColumns = ["albumId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    primaryKeys = ["songId", "albumId"]
)
data class SongAlbumRelationTable(
    val songId: Long,
    val albumId: Long
)
