package com.poulastaa.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EntityRelationAlbum(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val name: String,
    val poster: String? = null,
    val popularity: Long = 0,
)
