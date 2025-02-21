package com.poulastaa.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.poulastaa.core.domain.model.SongId

@Entity
data class EntityPrevSong(
    @PrimaryKey(autoGenerate = false)
    val id: SongId,
    val title: String,
    val poster: String? = null,
)
