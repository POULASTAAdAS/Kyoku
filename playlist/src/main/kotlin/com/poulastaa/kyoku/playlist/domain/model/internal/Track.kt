package com.poulastaa.kyoku.playlist.domain.model.internal

import com.google.gson.annotations.SerializedName

data class Track(
    @SerializedName("name")
    val name: String? = null,
)