package com.poulastaa.kyoku.data.model.database.table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AlbumTable")
data class AlbumTable(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val albumId: Long,
    val name: String,
    val points: Int = 0
)
