package com.poulastaa.kyoku.data.model.api.service

import kotlinx.serialization.Serializable

@Serializable
enum class HandleSpotifyPlaylistStatus {
    SUCCESS,
    FAILURE
}