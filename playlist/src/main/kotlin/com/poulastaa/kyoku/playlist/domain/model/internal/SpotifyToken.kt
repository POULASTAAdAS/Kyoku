package com.poulastaa.kyoku.playlist.domain.model.internal

import com.google.gson.annotations.SerializedName

data class SpotifyToken(
    @SerializedName("access_token")
    val accessToken: String? = null,
    @SerializedName("expires_in")
    val expiresIn: Long = 0,
)
