package com.poulastaa.network.model

import kotlinx.serialization.Serializable

@Serializable
enum class HistoryTypeDto {
    PLAYLIST,
    ALBUM,
    INDIVIDUAL
}