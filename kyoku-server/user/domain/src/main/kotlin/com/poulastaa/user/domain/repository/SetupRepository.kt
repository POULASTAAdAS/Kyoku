package com.poulastaa.user.domain.repository

import com.poulastaa.core.domain.model.*
import com.poulastaa.core.domain.repository.ArtistId
import com.poulastaa.core.domain.repository.GenreId

typealias SpotifySongTitle = String

interface SetupRepository {
    suspend fun getSpotifyPlaylist(
        userPayload: ReqUserPayload,
        spotifyPayload: List<SpotifySongTitle>,
    ): DtoFullPlaylist?

    suspend fun setBDate(
        userPayload: ReqUserPayload,
        bDate: String,
    ): Boolean

    suspend fun getGenre(page: Int, size: Int, query: String): List<DtoGenre>

    suspend fun upsertGenre(
        userPayload: ReqUserPayload,
        req: DtoUpsert<GenreId>,
    ): List<DtoGenre>

    suspend fun getArtist(
        page: Int,
        size: Int,
        query: String,
        payload: ReqUserPayload,
    ): List<DtoPrevArtist>

    suspend fun upsertArtist(
        userPayload: ReqUserPayload,
        req: DtoUpsert<ArtistId>,
    ): List<DtoArtist>
}