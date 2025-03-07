package com.poulastaa.view.data.repository

import com.poulastaa.core.domain.model.ReqUserPayload
import com.poulastaa.core.domain.repository.ArtistId
import com.poulastaa.core.domain.repository.view.LocalViewDatasource
import com.poulastaa.view.domain.model.ViewArtistPayload
import com.poulastaa.view.domain.repository.ViewRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

internal class ViewRepositoryService(
    private val db: LocalViewDatasource,
) : ViewRepository {
    override suspend fun getArtist(
        artistId: ArtistId,
        userPayload: ReqUserPayload,
    ): ViewArtistPayload? = coroutineScope {
        db.getUserByEmail(userPayload.email, userPayload.userType) ?: return@coroutineScope null

        val artist = async { db.getArtist(artistId) }
        val songs = async { db.getArtistMostPopularSongs(artistId) }

        ViewArtistPayload(
            artist = artist.await() ?: return@coroutineScope null,
            songs = songs.await(),
        )
    }
}