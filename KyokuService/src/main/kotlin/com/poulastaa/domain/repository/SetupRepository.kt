package com.poulastaa.domain.repository

import com.poulastaa.data.model.PlaylistDto
import com.poulastaa.domain.model.PlaylistResult

typealias SpotifySongTitle = String

interface SetupRepository {
    suspend fun getSpotifyPlaylist(
        spotifyPayload: List<SpotifySongTitle>,
    ): PlaylistResult
}