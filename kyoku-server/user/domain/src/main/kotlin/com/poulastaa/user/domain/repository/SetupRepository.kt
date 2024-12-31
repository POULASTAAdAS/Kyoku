package com.poulastaa.user.domain.repository

import com.poulastaa.core.domain.model.PlaylistFullDto
import com.poulastaa.core.domain.model.ReqUserPayload

typealias SpotifySongTitle = String


interface SetupRepository {
    suspend fun getSpotifyPlaylist(
        userPayload: ReqUserPayload,
        spotifyPayload: List<SpotifySongTitle>,
    ): PlaylistFullDto?
}