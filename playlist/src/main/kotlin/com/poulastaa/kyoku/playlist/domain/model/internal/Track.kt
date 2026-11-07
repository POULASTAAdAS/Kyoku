package com.poulastaa.kyoku.playlist.domain.model.internal

import com.google.gson.annotations.SerializedName

data class Track(
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("album")
    val album: NamedItem? = null,
    @SerializedName("artists")
    val artists: List<NamedItem>? = null,
)

data class NamedItem(
    @SerializedName("name")
    val name: String? = null,
)
