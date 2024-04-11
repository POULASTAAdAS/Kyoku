package com.poulastaa.kyoku.data.model.database.table

import androidx.room.Entity
import androidx.room.ForeignKey
import com.poulastaa.kyoku.data.model.database.table.prev.ArtistSongTable


@Entity(
    tableName = "ArtistPreviewSongRelation",
    foreignKeys = [
        ForeignKey(
            entity = ArtistTable::class,
            parentColumns = ["artistId"],
            childColumns = ["artistId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ArtistSongTable::class,
            parentColumns = ["id"],
            childColumns = ["songId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    primaryKeys = ["artistId", "songId"]
)
data class ArtistPreviewSongRelation(
    val artistId: Long,
    val songId: Long
)
