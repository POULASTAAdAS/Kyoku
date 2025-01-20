package com.poulastaa.user.domain.repository

import com.poulastaa.core.domain.model.DtoGenre
import com.poulastaa.core.domain.model.DtoPlaylistFull
import com.poulastaa.core.domain.model.ReqUserPayload

typealias SpotifySongTitle = String

interface SetupRepository {
    suspend fun getSpotifyPlaylist(
        userPayload: ReqUserPayload,
        spotifyPayload: List<SpotifySongTitle>,
    ): DtoPlaylistFull?

    suspend fun setBDate(
        userPayload: ReqUserPayload,
        bDate: String,
    ): Boolean

    suspend fun getGenre(genreIds: List<Int>): List<DtoGenre>
}