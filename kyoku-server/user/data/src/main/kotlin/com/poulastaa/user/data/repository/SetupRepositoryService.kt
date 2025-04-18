package com.poulastaa.user.data.repository

import com.poulastaa.core.domain.model.*
import com.poulastaa.core.domain.repository.ArtistId
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
    ): DtoFullPlaylist? {
        val user = db.getUserByEmail(userPayload.email, userPayload.userType) ?: return null
        return db.createPlaylistFromSpotifyPlaylist(user, spotifyPayload)
    }

    override suspend fun setBDate(
        userPayload: ReqUserPayload,
        bDate: String,
    ): Boolean {
        val user = db.getUserByEmail(userPayload.email, userPayload.userType) ?: return false
        return db.updateBDate(user, userPayload.userType, bDate)
    }

    override suspend fun getGenre(
        page: Int,
        size: Int,
        query: String,
    ): List<DtoGenre> = db.getPagingGenre(page, size, query)

    override suspend fun upsertGenre(
        userPayload: ReqUserPayload,
        req: DtoUpsert<GenreId>,
    ): List<DtoGenre> {
        if (req.idList.isEmpty()) return emptyList()

        val user = db.getUserByEmail(userPayload.email, userPayload.userType) ?: return emptyList()
        return db.upsertGenre(user, req)
    }

    override suspend fun getArtist(
        page: Int, size: Int,
        query: String,
        payload: ReqUserPayload,
    ): List<DtoPrevArtist> {
        val user = db.getUserByEmail(payload.email, payload.userType) ?: return emptyList()
        return db.getPagingArtist(page, size, query, user.id)
    }

    override suspend fun upsertArtist(
        userPayload: ReqUserPayload,
        req: DtoUpsert<ArtistId>,
    ): List<DtoArtist> {
        if (req.idList.isEmpty()) return emptyList()

        val user = db.getUserByEmail(userPayload.email, userPayload.userType) ?: return emptyList()
        return db.upsertArtist(user, req)
    }

    override suspend fun getBDate(userPayload: ReqUserPayload): String? {
        val user = db.getUserByEmail(userPayload.email, userPayload.userType) ?: return null
        return user.bDate?.toString()
    }

    override suspend fun updateUsername(
        userPayload: ReqUserPayload,
        username: String,
    ): Boolean? {
        val user = db.getUserByEmail(userPayload.email, userPayload.userType) ?: return null
        if (user.userName == username) return true
        db.updateUsername(username, user.id, userPayload.userType).also { return true }
    }
}