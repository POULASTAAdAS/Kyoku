package com.poulastaa.data.model

import kotlinx.serialization.Serializable

@Serializable
enum class SongPagingTypeDto {
    TITLE,
    POPULARITY,
    ARTIST,
}