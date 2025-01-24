package com.poulastaa.user.data.repository

import com.poulastaa.core.domain.model.*
import com.poulastaa.core.domain.repository.GenreId
import com.poulastaa.core.domain.repository.setup.LocalSetupDatasource
import com.poulastaa.user.domain.repository.SetupRepository
import com.poulastaa.user.domain.repository.SpotifySongTitle

class SetupRepositoryService(
    private val db: LocalSetupDatasource,
) : SetupRepository {
    override suspend fun getSpotifyPlaylist(
        userPayload: ReqUserPayload,
        spotifyPayload: List<SpotifySongTitle>,
    ): DtoPlaylistFull? {
        val user = db.getUserByEmail(userPayload.email, userPayload.userType) ?: return null
        return db.createPlaylistFromSpotifyPlaylist(user, spotifyPayload)
    }

    override suspend fun setBDate(
        userPayload: ReqUserPayload,
        bDate: String,
    ): Boolean {
        val user = db.getUserByEmail(userPayload.email, userPayload.userType) ?: return false
        return db.updateBDate(user, bDate)
    }

    override suspend fun getGenre(
        page: Int,
        size: Int,
        query: String,
    ): List<DtoGenre> = db.getPagingGenre(page, size, query)

    override suspend fun upsertGenre(
        userPayload: ReqUserPayload,
        req: List<DtoUpsert<GenreId>>,
    ): List<DtoGenre> {
        val user = db.getUserByEmail(userPayload.email, userPayload.userType) ?: return emptyList()
        return db.upsertGenre(user, req)
    }

    override suspend fun getArtist(
        page: Int,
        size: Int,
        query: String,
    ): List<DtoPrevArtist> = db.getPagingArtist(page, size, query)
}