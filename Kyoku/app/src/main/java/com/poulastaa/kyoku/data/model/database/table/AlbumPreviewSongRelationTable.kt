package com.poulastaa.kyoku.data.model.database.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey


@Entity(
    tableName = "AlbumPreviewSongRelationTable",
    foreignKeys = [
        ForeignKey(
            entity = AlbumTable::class,
            parentColumns = ["id"],
            childColumns = ["albumId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SongPreviewTable::class,
            parentColumns = ["id"],
            childColumns = ["songId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    primaryKeys = ["albumId", "songId"]
)
data class AlbumPreviewSongRelationTable(
    @ColumnInfo(index = true)
    val albumId: Long,
    @ColumnInfo(index = true)
    val songId: Long
)
