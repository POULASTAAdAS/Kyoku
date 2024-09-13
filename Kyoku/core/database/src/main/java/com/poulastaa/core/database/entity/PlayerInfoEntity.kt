package com.poulastaa.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.poulastaa.core.domain.RepeatState

@Entity
data class PlayerInfoEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long = 1,
    val type: String = "",
    val isShuffledEnabled: Boolean = false,
    val repeatState: RepeatState = RepeatState.IDLE,
    val isPlaying: Boolean = false,
    val hasNext: Boolean = false,
    val hasPrev: Boolean = false,
)