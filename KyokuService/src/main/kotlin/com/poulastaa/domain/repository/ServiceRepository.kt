package com.poulastaa.domain.repository

import com.poulastaa.data.model.PlaylistDto
import com.poulastaa.domain.model.ReqUserPayload

interface ServiceRepository {
    suspend fun getSpotifyPlaylist(
        userPayload: ReqUserPayload,
        spotifyPayload: List<SpotifySongTitle>,
    ): PlaylistDto

    suspend fun updateBDate(
        userPayload: ReqUserPayload,
        date: Long,
    ): Boolean
}