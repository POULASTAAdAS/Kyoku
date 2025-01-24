package com.poulastaa.user.domain.repository

import com.poulastaa.core.domain.model.DtoGenre
import com.poulastaa.core.domain.model.DtoPlaylistFull
import com.poulastaa.core.domain.model.DtoUpsert
import com.poulastaa.core.domain.model.ReqUserPayload
import com.poulastaa.core.domain.repository.GenreId
import com.poulastaa.core.domain.model.DtoPrevArtist

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

    suspend fun getGenre(page: Int, size: Int, query: String): List<DtoGenre>

    suspend fun upsertGenre(
        userPayload: ReqUserPayload,
        req: List<DtoUpsert<GenreId>>,
    ): List<DtoGenre>

    suspend fun getArtist(page: Int, size: Int, query: String): List<DtoPrevArtist>
}