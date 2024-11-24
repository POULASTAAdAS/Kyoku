package com.poulastaa.network.model

import kotlinx.serialization.Serializable

@Serializable
enum class AlbumPagingTypeDto {
    NAME,
    BY_YEAR,
    BY_POPULARITY
}