package com.poulastaa.core.database.repository.item

import com.poulastaa.core.database.SQLDbManager.kyokuDbQuery
import com.poulastaa.core.database.SQLDbManager.userDbQuery
import com.poulastaa.core.database.dao.DaoPlaylist
import com.poulastaa.core.database.entity.user.RelationEntityUserPlaylist
import com.poulastaa.core.database.mapper.toDtoPlaylist
import com.poulastaa.core.domain.model.DtoArtist
import com.poulastaa.core.domain.model.DtoDBUser
import com.poulastaa.core.domain.model.DtoPlaylist
import com.poulastaa.core.domain.model.UserType
import com.poulastaa.core.domain.repository.ArtistId
import com.poulastaa.core.domain.repository.LocalCoreDatasource
import com.poulastaa.core.domain.repository.UserId
import com.poulastaa.core.domain.repository.item.LocalItemCacheDatasource
import com.poulastaa.core.domain.repository.item.LocalItemDatasource
import org.jetbrains.exposed.sql.insert

internal class ExposedLocalItemDatasource(
    private val core: LocalCoreDatasource,
    private val cache: LocalItemCacheDatasource,
) : LocalItemDatasource {
    override suspend fun getUserByEmail(
        email: String,
        userType: UserType,
    ): DtoDBUser? = core.getUserByEmail(email, userType)

    override suspend fun getArtistById(artistId: ArtistId): DtoArtist? =
        cache.cacheArtistById(artistId) ?: core.getArtistOnId(artistId)?.also {
            cache.setArtistById(it)
        }

    override suspend fun createPlaylist(
        playlistName: String,
        userId: UserId,
    ): DtoPlaylist? = kyokuDbQuery {
        DaoPlaylist.new {
            this.name = playlistName
            this.isPublic = false
        }
    }.also { dao ->
        userDbQuery {
            RelationEntityUserPlaylist.insert {
                it[this.playlistId] = dao.id.value
                it[this.userId] = userId
            }
        }
    }.toDtoPlaylist().also {
        cache.setPlaylistById(it)
    }
}