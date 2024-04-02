package com.poulastaa.kyoku.data.model.database.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "DailyMixTable")
data class DailyMixTable(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val songId: Long = 0,
    val title: String = "",
    val artist: String = "",
    val album: String = "",
    val coverImage: String = "",
    val year: String = "",

    @ColumnInfo(name = "pos")
    val pos: Int = 0
)
