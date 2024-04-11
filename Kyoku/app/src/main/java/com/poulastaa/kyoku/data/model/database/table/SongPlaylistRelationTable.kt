package com.poulastaa.kyoku.data.model.database.table

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "SongPlaylistRelationTable",
    foreignKeys = [
        ForeignKey(
            entity = PlaylistTable::class,
            parentColumns = ["id"],
            childColumns = ["playlistId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PlaylistSongTable::class,
            parentColumns = ["songId"],
            childColumns = ["songId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    primaryKeys = ["playlistId", "songId"]
)
data class SongPlaylistRelationTable(
    val playlistId: Long = 0,
    val songId: Long = 0
)


