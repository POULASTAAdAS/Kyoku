package com.poulastaa.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.poulastaa.core.domain.model.SongId

@Entity(
    primaryKeys = ["typeId", "dataId"],
    foreignKeys = [
        ForeignKey(
            entity = EntityPrevSong::class,
            parentColumns = ["id"],
            childColumns = ["dataId"]
        )
    ]
)
data class EntityPrevExplore(
    @ColumnInfo(index = true)
    val typeId: Int,
    @ColumnInfo(index = true)
    val dataId: SongId,
)