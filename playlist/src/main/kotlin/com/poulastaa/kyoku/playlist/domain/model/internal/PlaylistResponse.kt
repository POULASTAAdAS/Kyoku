package com.poulastaa.kyoku.playlist.domain.model.internal

import com.google.gson.annotations.SerializedName

data class PlaylistResponse(
    @SerializedName("items")
    val items: List<PlaylistItem>? = null,
)