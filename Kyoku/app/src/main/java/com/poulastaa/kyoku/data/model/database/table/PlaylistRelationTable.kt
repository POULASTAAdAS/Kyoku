package com.poulastaa.kyoku.data.model.database.table

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "PlaylistRelationTable",
    foreignKeys = [
        ForeignKey(
            entity = PlaylistTable::class,
            parentColumns = ["id"],
            childColumns = ["playlistId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SongTable::class,
            parentColumns = ["id"],
            childColumns = ["songId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PlaylistRelationTable(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val playlistId: Long = 0,
    val songId: Long = 0
)
