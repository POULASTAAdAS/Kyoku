package com.poulastaa.kyoku.data.model.database.table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AlbumPrevTable")
data class AlbumPrevTable(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val albumId: Long = 0,
    val name: String = ""
)
