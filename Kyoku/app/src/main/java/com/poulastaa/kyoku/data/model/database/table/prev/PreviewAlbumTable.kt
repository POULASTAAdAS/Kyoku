package com.poulastaa.kyoku.data.model.database.table.prev

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PreviewAlbumTable")
data class PreviewAlbumTable(
    @PrimaryKey
    val albumId: Long,
    val name: String,
    val coverImage: String
)
