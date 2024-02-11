package com.poulastaa.kyoku.data.model.database.table

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "PlaylistTable",
    foreignKeys = [
        ForeignKey(
            entity = SongTable::class,
            parentColumns = ["id"],
            childColumns = ["songId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(
            value = ["name"], unique = true
        )
    ]
)
data class PlaylistTable(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String = "",
    val songId: Long = 0
)
