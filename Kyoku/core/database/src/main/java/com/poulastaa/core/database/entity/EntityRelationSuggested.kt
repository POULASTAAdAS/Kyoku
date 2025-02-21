package com.poulastaa.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.poulastaa.core.domain.model.DtoSuggestedType

@Entity(
    primaryKeys = ["typeId", "dataId"],
)
data class EntityRelationSuggested(
    @ColumnInfo(index = true)
    val typeId: DtoSuggestedType,
    @ColumnInfo(index = true)
    val dataId: Long,
)
