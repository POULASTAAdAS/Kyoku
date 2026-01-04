package com.poulastaa.kyoku.playlist.domain.model.internal

import com.google.gson.annotations.SerializedName

data class PlaylistItem(
    @SerializedName("track")
    val track: Track? = null,
)