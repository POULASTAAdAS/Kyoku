package com.poulastaa.data.model

import kotlinx.serialization.Serializable

@Serializable
enum class AlbumPagingTypeDto {
    NAME,
    BY_YEAR,
    BY_POPULARITY
}