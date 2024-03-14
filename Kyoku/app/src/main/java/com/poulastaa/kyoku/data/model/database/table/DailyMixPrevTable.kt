package com.poulastaa.kyoku.data.model.database.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "DailyMixPrevTable",
    foreignKeys = [
        ForeignKey(
            entity = SongPreviewTable::class,
            parentColumns = ["id"],
            childColumns = ["id"]
        )
    ]
)
data class DailyMixPrevTable(
    @PrimaryKey
    @ColumnInfo(index = true)
    val id: Long
)
