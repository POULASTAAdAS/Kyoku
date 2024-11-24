package com.poulastaa.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.poulastaa.core.domain.PlayType
import com.poulastaa.core.domain.RepeatState

@Entity
data class PlayerInfoEntity(
    @PrimaryKey(autoGenerate = false)
    val otherId: Long = 1,
    val title: String = "",
    val type: PlayType = PlayType.IDLE,
    val isShuffledEnabled: Boolean = false,
    val repeatState: RepeatState = RepeatState.IDLE,
    val isPlaying: Boolean = false,
    val hasNext: Boolean = false,
    val hasPrev: Boolean = false,
)