package com.poulastaa.suggestion.network.model

import kotlinx.serialization.Serializable

@Serializable
internal enum class AddSongToPlaylistPageTypeResponse {
    YOUR_FAVOURITES,
    SUGGESTED_FOR_YOU,
    YOU_MAY_ALSO_LIKE,
}