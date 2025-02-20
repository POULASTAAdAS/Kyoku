package com.poulastaa.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.poulastaa.core.domain.model.AlbumId

@Entity
data class EntityPrevAlbum(
    @PrimaryKey(autoGenerate = false)
    val id: AlbumId,
    val title: String,
    val poster: String? = null,
)
