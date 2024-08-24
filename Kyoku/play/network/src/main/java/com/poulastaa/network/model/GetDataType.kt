package com.poulastaa.network.model

import kotlinx.serialization.Serializable

@Serializable
enum class GetDataType {
    FEV,
    ARTIST_MIX,
    OLD_MIX,
    POPULAR_MIX,
    PLAYLIST,
    ALBUM
}