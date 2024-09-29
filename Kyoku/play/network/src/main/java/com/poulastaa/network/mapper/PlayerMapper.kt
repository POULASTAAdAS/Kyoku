package com.poulastaa.network.mapper

import com.poulastaa.core.domain.RecentHistoryOtherType
import com.poulastaa.network.model.HistoryTypeDto

fun RecentHistoryOtherType.toHistoryTypeDto() = when(this){
    RecentHistoryOtherType.PLAYLIST -> HistoryTypeDto.PLAYLIST
    RecentHistoryOtherType.ALBUM -> HistoryTypeDto.ALBUM
    RecentHistoryOtherType.INDIVIDUAL -> HistoryTypeDto.INDIVIDUAL
}