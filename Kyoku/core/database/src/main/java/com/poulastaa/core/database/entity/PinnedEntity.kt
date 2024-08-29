package com.poulastaa.core.database.entity

import androidx.room.Entity
import com.poulastaa.core.domain.model.PinnedType

@Entity(
    primaryKeys = ["id", "type"]
)
data class PinnedEntity(
    val id: Long,
    val type: PinnedType
)
