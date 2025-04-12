package com.poulastaa.search.model

import kotlinx.serialization.Serializable

@Serializable
internal enum class RequestExploreAlbumFilterType {
    MOST_POPULAR,
    ARTIST,
    RELEASE_YEAR,
}