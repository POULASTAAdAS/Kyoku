package com.poulastaa.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.poulastaa.core.domain.model.ArtistId

@Entity
data class EntityPrevArtist(
    @PrimaryKey(autoGenerate = false)
    val id: ArtistId,
    val name: String,
    val coverImage: String? = null,
)
