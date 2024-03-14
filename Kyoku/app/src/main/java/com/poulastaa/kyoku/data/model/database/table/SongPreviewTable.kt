package com.poulastaa.kyoku.data.model.database.table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SongPreviewTable")
class SongPreviewTable(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val songId: Long = 0,
    val title: String = "",
    val coverImage: String = "",
    val artist: String = "",
    val album: String = ""
)
