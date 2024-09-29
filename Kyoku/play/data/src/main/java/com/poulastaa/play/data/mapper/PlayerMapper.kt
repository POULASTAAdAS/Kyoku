package com.poulastaa.play.data.mapper

import com.poulastaa.core.domain.PlayType
import com.poulastaa.core.domain.RecentHistoryOtherType

fun PlayType.toRecentHistoryOtherType() = when (this) {
    PlayType.PLAYLIST -> RecentHistoryOtherType.PLAYLIST
    PlayType.ALBUM -> RecentHistoryOtherType.ALBUM
    PlayType.IDLE -> throw Exception("This should not happen")
    else -> RecentHistoryOtherType.INDIVIDUAL
}