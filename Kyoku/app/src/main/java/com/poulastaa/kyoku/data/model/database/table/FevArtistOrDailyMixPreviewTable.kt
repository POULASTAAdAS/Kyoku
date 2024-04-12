package com.poulastaa.kyoku.data.model.database.table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FevArtistOrDailyMixPreviewTable")
data class FevArtistOrDailyMixPreviewTable(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val artist: String = "",
    val coverImage: String = "",
    val type: MixType = MixType.ARTIST_MIX
)

enum class MixType {
    ARTIST_MIX,
    DAILY_MIX
}
