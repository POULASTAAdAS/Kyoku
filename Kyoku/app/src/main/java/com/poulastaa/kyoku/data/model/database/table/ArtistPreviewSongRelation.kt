package com.poulastaa.kyoku.data.model.database.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey


@Entity(
    tableName = "ArtistPreviewSongRelation",
    foreignKeys = [
        ForeignKey(
            entity = ArtistTable::class,
            parentColumns = ["id"],
            childColumns = ["artistId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SongPreviewTable::class,
            parentColumns = ["id"],
            childColumns = ["songId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    primaryKeys = ["artistId", "songId"]
)
data class ArtistPreviewSongRelation(
    @ColumnInfo(index = true)
    val artistId: Long,
    @ColumnInfo(index = true)
    val songId: Long
)
