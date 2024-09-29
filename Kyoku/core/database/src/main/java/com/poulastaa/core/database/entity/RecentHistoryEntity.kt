package com.poulastaa.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.poulastaa.core.domain.RecentHistoryOtherType
import java.time.LocalDateTime

@Entity
data class RecentHistoryEntity(
    @PrimaryKey(autoGenerate = false)
    val timeStamp: LocalDateTime = LocalDateTime.now(),
    val songId: Long = -1,
    val otherId: Long = -1,
    val otherType: RecentHistoryOtherType = RecentHistoryOtherType.INDIVIDUAL,
)
