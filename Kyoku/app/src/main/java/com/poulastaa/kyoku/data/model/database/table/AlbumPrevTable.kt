package com.poulastaa.kyoku.data.model.database.table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AlbumPrevTable")
data class AlbumPrevTable(
    @PrimaryKey
    val albumId: Long,
    val name: String = ""
)
