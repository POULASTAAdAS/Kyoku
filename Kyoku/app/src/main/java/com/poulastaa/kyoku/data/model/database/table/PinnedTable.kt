package com.poulastaa.kyoku.data.model.database.table

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "PinnedTable",
    foreignKeys = [
        ForeignKey(
            entity = PlaylistTable::class,
            parentColumns = ["id"],
            childColumns = ["playlistId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = AlbumTable::class,
            parentColumns = ["id"],
            childColumns = ["albumId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ArtistPrevTable::class,
            parentColumns = ["id"],
            childColumns = ["artistId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PinnedTable(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val playlistId: Long? = null,
    val albumId: Long? = null,
    val artistId: Long? = null
)
