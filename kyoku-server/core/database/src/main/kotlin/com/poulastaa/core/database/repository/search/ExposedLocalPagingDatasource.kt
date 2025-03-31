package com.poulastaa.core.database.repository.search

import com.poulastaa.core.database.SQLDbManager.kyokuDbQuery
import com.poulastaa.core.database.entity.app.*
import com.poulastaa.core.domain.model.DtoArtistPagingItem
import com.poulastaa.core.domain.repository.ArtistId
import com.poulastaa.core.domain.repository.search.LocalArtistPagingDatasource
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and

internal class ExposedLocalArtistPagingDatasource : LocalArtistPagingDatasource {
    override suspend fun getPagingSong(
        page: Int,
        size: Int,
        query: String?,
        artistId: ArtistId,
    ): List<DtoArtistPagingItem> = kyokuDbQuery {
        RelationEntitySongArtist
            .join(
                otherTable = EntitySong,
                joinType = JoinType.INNER,
                onColumn = RelationEntitySongArtist.songId,
                otherColumn = EntitySong.id,
                additionalConstraint = {
                    EntitySong.id eq RelationEntitySongArtist.songId
                }
            ).join(
                otherTable = EntitySongInfo,
                joinType = JoinType.INNER,
                onColumn = EntitySongInfo.songId,
                otherColumn = EntitySong.id,
                additionalConstraint = {
                    EntitySong.id eq EntitySongInfo.songId
                }
            ).select(
                EntitySong.id,
                EntitySong.title,
                EntitySong.poster,
                EntitySongInfo.releaseYear
            ).where {
                if (query.isNullOrBlank()) RelationEntitySongArtist.artistId eq artistId
                else RelationEntitySongArtist.artistId eq artistId and (EntitySong.title like "%$query%")
            }.orderBy(EntitySongInfo.releaseYear to SortOrder.DESC)
            .offset(if (page == 1) 0L else (page * size).toLong())
            .limit(size)
            .map {
                DtoArtistPagingItem(
                    id = it[EntitySong.id].value,
                    title = it[EntitySong.title],
                    rawPoster = it[EntitySong.poster],
                    releaseYear = it[EntitySongInfo.releaseYear]
                )
            }
    }


    override suspend fun getPagingAlbum(
        page: Int,
        size: Int,
        query: String?,
        artistId: ArtistId,
    ): List<DtoArtistPagingItem> = kyokuDbQuery {
        RelationEntitySongArtist
            .join(
                otherTable = EntitySong,
                joinType = JoinType.INNER,
                onColumn = RelationEntitySongArtist.songId,
                otherColumn = EntitySong.id,
                additionalConstraint = {
                    EntitySong.id eq RelationEntitySongArtist.songId
                }
            ).join(
                otherTable = EntitySongInfo,
                joinType = JoinType.INNER,
                onColumn = EntitySongInfo.songId,
                otherColumn = EntitySong.id,
                additionalConstraint = {
                    EntitySong.id eq EntitySongInfo.songId
                }
            ).join(
                otherTable = RelationEntitySongAlbum,
                joinType = JoinType.INNER,
                onColumn = RelationEntitySongAlbum.songId,
                otherColumn = EntitySong.id,
                additionalConstraint = {
                    RelationEntitySongAlbum.songId eq EntitySong.id
                }
            ).join(
                otherTable = EntityAlbum,
                joinType = JoinType.INNER,
                onColumn = EntityAlbum.id,
                otherColumn = RelationEntitySongAlbum.albumId,
                additionalConstraint = {
                    EntityAlbum.id eq RelationEntitySongAlbum.albumId
                }
            ).select(
                EntitySong.poster,
                EntitySongInfo.releaseYear,
                EntityAlbum.id,
                EntityAlbum.name
            ).where {
                if (query.isNullOrBlank()) RelationEntitySongArtist.artistId eq artistId
                else RelationEntitySongArtist.artistId eq artistId and (EntitySong.title like "%$query%")
            }
            .groupBy(EntityAlbum.id, EntitySong.poster, EntitySongInfo.releaseYear, EntityAlbum.name)
            .orderBy(EntitySongInfo.releaseYear to SortOrder.DESC)
            .offset(if (page == 1) 0L else (page * size).toLong())
            .limit(size)
            .map {
                DtoArtistPagingItem(
                    id = it[EntityAlbum.id].value,
                    title = it[EntityAlbum.name],
                    rawPoster = it[EntitySong.poster],
                    releaseYear = it[EntitySongInfo.releaseYear]
                )
            }
    }
}