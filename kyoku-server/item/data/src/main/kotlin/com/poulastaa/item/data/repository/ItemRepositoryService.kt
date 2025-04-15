package com.poulastaa.item.data.repository

import com.poulastaa.core.domain.model.DtoArtist
import com.poulastaa.core.domain.model.DtoPlaylist
import com.poulastaa.core.domain.model.ReqUserPayload
import com.poulastaa.core.domain.repository.ArtistId
import com.poulastaa.core.domain.repository.item.LocalItemDatasource
import com.poulastaa.item.domain.repository.ItemRepository

internal class ItemRepositoryService(
    private val db: LocalItemDatasource,
) : ItemRepository {
    override suspend fun getArtist(artistId: ArtistId): DtoArtist? = db.getArtistById(artistId)
    override suspend fun createPlaylist(
        playlistName: String,
        payload: ReqUserPayload,
    ): DtoPlaylist? {
        val user = db.getUserByEmail(payload.email, payload.userType) ?: return null
        return db.createPlaylist(playlistName, user.id)
    }
}