package com.poulastaa.core.database.repository.view

import com.poulastaa.core.database.SQLDbManager.kyokuDbQuery
import com.poulastaa.core.database.SQLDbManager.shardPopularDbQuery
import com.poulastaa.core.database.dao.DaoArtist
import com.poulastaa.core.database.entity.app.EntityArtist
import com.poulastaa.core.database.entity.shard.suggestion.ShardEntityArtistPopularSong
import com.poulastaa.core.database.mapper.toDtoPrevArtist
import com.poulastaa.core.domain.model.DtoDBUser
import com.poulastaa.core.domain.model.DtoDetailedPrevSong
import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.core.domain.model.UserType
import com.poulastaa.core.domain.repository.ArtistId
import com.poulastaa.core.domain.repository.LocalCoreDatasource
import com.poulastaa.core.domain.repository.SongId
import com.poulastaa.core.domain.repository.view.LocalViewCacheDatasource
import com.poulastaa.core.domain.repository.view.LocalViewDatasource

internal class ExposedLocalViewDatasource(
    private val core: LocalCoreDatasource,
    private val cache: LocalViewCacheDatasource,
) : LocalViewDatasource {
    override suspend fun getUserByEmail(
        email: String,
        userType: UserType,
    ): DtoDBUser? = core.getUserByEmail(email, userType)

    override suspend fun getArtist(artistId: ArtistId): DtoPrevArtist? =
        cache.cacheArtistById(artistId) ?: kyokuDbQuery {
            DaoArtist.find {
                EntityArtist.id eq artistId
            }.map { it.toDtoPrevArtist() }
        }.first()

    override suspend fun getArtistMostPopularSongs(artistId: ArtistId): List<DtoDetailedPrevSong> {
        val songIdList = shardPopularDbQuery {
            ShardEntityArtistPopularSong.select(ShardEntityArtistPopularSong.id).where {
                ShardEntityArtistPopularSong.artistId eq artistId
            }.map { it[ShardEntityArtistPopularSong.id].value as SongId }
        }

        val songs = songIdList.let { cache.cacheDetailedPrevSongById(it) }.ifEmpty {
            core.getDetailedPrevSongOnId(songIdList)
        }

        return if (songs.size == songIdList.size) return songs
        else {
            val notFound = songIdList.filterNot { songs.map { it.id }.contains(it) }
            songs + core.getDetailedPrevSongOnId(notFound).also { cache.setDetailedPrevSongById(it) }
        }
    }
}