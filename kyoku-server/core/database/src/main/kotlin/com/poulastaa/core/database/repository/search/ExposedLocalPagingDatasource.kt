package com.poulastaa.core.database.repository.search

import com.poulastaa.core.database.SQLDbManager.kyokuDbQuery
import com.poulastaa.core.database.SQLDbManager.shardSearchDbQuery
import com.poulastaa.core.database.dao.DaoPlaylist
import com.poulastaa.core.database.dao.DaoSong
import com.poulastaa.core.database.entity.app.EntityPlaylist
import com.poulastaa.core.database.entity.app.EntitySong
import com.poulastaa.core.database.entity.app.RelationEntitySongPlaylist
import com.poulastaa.core.database.entity.shard.paging.*
import com.poulastaa.core.domain.model.DtoExploreAlbumFilterType
import com.poulastaa.core.domain.model.DtoExploreArtistFilterType
import com.poulastaa.core.domain.model.DtoSearchItem
import com.poulastaa.core.domain.repository.ArtistId
import com.poulastaa.core.domain.repository.search.LocalPagingDatasource
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.jetbrains.exposed.sql.*

internal class ExposedLocalPagingDatasource : LocalPagingDatasource {
    override suspend fun getArtistPagingSong(
        page: Int,
        size: Int,
        query: String?,
        artistId: ArtistId,
    ): List<DtoSearchItem> = shardSearchDbQuery {
        ShardPagingEntitySong.join(
            otherTable = ShardPagingRelationEntityArtistSong,
            joinType = JoinType.INNER,
            onColumn = ShardPagingEntitySong.id,
            otherColumn = ShardPagingRelationEntityArtistSong.songId,
            additionalConstraint = {
                ShardPagingEntitySong.id eq ShardPagingRelationEntityArtistSong.songId
            }
        ).select(
            ShardPagingEntitySong.id,
            ShardPagingEntitySong.title,
            ShardPagingEntitySong.poster,
            ShardPagingEntitySong.releaseYear
        ).where {
            if (query.isNullOrBlank()) ShardPagingRelationEntityArtistSong.artistId eq artistId
            else ShardPagingRelationEntityArtistSong.artistId eq artistId and (ShardPagingEntitySong.title like "$query%")
        }.orderBy(ShardPagingEntitySong.releaseYear to SortOrder.DESC)
            .offset(if (page == 1) 0L else (page * size).toLong())
            .limit(size)
            .map {
                DtoSearchItem(
                    id = it[ShardPagingEntitySong.id].value,
                    title = it[ShardPagingEntitySong.title],
                    rawPoster = it[ShardPagingEntitySong.poster],
                    releaseYear = it[ShardPagingEntitySong.releaseYear]
                )
            }
    }

    override suspend fun getArtistPagingAlbum(
        page: Int,
        size: Int,
        query: String?,
        artistId: ArtistId,
    ): List<DtoSearchItem> = shardSearchDbQuery {
        ShardPagingEntityAlbum.join(
            otherTable = ShardPagingRelationEntityArtistAlbum,
            joinType = JoinType.INNER,
            onColumn = ShardPagingEntityAlbum.id,
            otherColumn = ShardPagingRelationEntityArtistAlbum.albumId,
            additionalConstraint = {
                ShardPagingEntityAlbum.id eq ShardPagingRelationEntityArtistAlbum.albumId
            }
        ).select(
            ShardPagingEntityAlbum.id,
            ShardPagingEntityAlbum.title,
            ShardPagingEntityAlbum.poster,
            ShardPagingEntityAlbum.releaseYear
        ).where {
            if (query.isNullOrBlank()) ShardPagingRelationEntityArtistAlbum.artistId eq artistId
            else ShardPagingRelationEntityArtistAlbum.artistId eq artistId and (ShardPagingEntityAlbum.title like "$query%")
        }.orderBy(ShardPagingEntityAlbum.releaseYear to SortOrder.DESC)
            .offset(if (page == 1) 0L else (page * size).toLong())
            .limit(size)
            .map {
                DtoSearchItem(
                    id = it[ShardPagingEntityAlbum.id].value,
                    title = it[ShardPagingEntityAlbum.title],
                    rawPoster = it[ShardPagingEntityAlbum.poster],
                    releaseYear = it[ShardPagingEntityAlbum.releaseYear]
                )
            }
    }

    override suspend fun getPagingAlbum(
        query: String?,
        page: Int,
        size: Int,
        filterType: DtoExploreAlbumFilterType,
    ): List<DtoSearchItem> = coroutineScope {
        val q = shardSearchDbQuery {
            ShardPagingEntityAlbum.select(
                ShardPagingEntityAlbum.id,
                ShardPagingEntityAlbum.title,
                ShardPagingEntityAlbum.poster,
                ShardPagingEntityAlbum.releaseYear,
                ShardPagingEntityAlbum.popularity
            )
        }

        when (filterType) {
            DtoExploreAlbumFilterType.MOST_POPULAR -> shardSearchDbQuery {
                q.let {
                    if (query != null && query.trim() != "" && query.isNotEmpty()) it.where {
                        ShardPagingEntityAlbum.title like "${query}%"
                    } else it
                }.orderBy(
                    ShardPagingEntityAlbum.title to SortOrder.ASC,
                    ShardPagingEntityAlbum.popularity to SortOrder.DESC
                ).offset(if (page == 1) 0L else (page * size).toLong())
                    .limit(size)
                    .map {
                        DtoSearchItem(
                            id = it[ShardPagingEntityAlbum.id].value,
                            title = it[ShardPagingEntityAlbum.title],
                            rawPoster = it[ShardPagingEntityAlbum.poster],
                            releaseYear = it[ShardPagingEntityAlbum.releaseYear]
                        )
                    }
            }

            DtoExploreAlbumFilterType.ARTIST -> shardSearchDbQuery {
                val qu = ShardPagingRelationEntityArtistAlbum.join(
                    otherTable = ShardPagingEntityArtist,
                    joinType = JoinType.INNER,
                    onColumn = ShardPagingRelationEntityArtistAlbum.artistId,
                    otherColumn = ShardPagingEntityArtist.id,
                    additionalConstraint = {
                        ShardPagingRelationEntityArtistAlbum.artistId eq ShardPagingEntityArtist.id
                    }
                ).join(
                    otherTable = ShardPagingEntityAlbum,
                    joinType = JoinType.INNER,
                    onColumn = ShardPagingRelationEntityArtistAlbum.albumId,
                    otherColumn = ShardPagingEntityAlbum.id,
                    additionalConstraint = {
                        ShardPagingRelationEntityArtistAlbum.albumId eq ShardPagingEntityAlbum.id
                    }
                ).select(
                    ShardPagingEntityAlbum.id,
                    ShardPagingEntityAlbum.title,
                    ShardPagingEntityAlbum.poster,
                    ShardPagingEntityAlbum.releaseYear,
                    ShardPagingEntityArtist.name,
                )

                qu.let {
                    if (query != null && query.trim() != "" && query.isNotEmpty()) it.where {
                        ShardPagingEntityAlbum.title like "${query}%"
                    } else it
                }.orderBy(ShardPagingEntityArtist.name)
                    .offset(if (page == 1) 0L else (page * size).toLong())
                    .limit(size)
                    .map {
                        DtoSearchItem(
                            id = it[ShardPagingEntityAlbum.id].value,
                            title = it[ShardPagingEntityAlbum.title],
                            rawPoster = it[ShardPagingEntityAlbum.poster],
                            releaseYear = it[ShardPagingEntityAlbum.releaseYear],
                            artist = it[ShardPagingEntityArtist.name]
                        )
                    }
            }

            DtoExploreAlbumFilterType.RELEASE_YEAR -> shardSearchDbQuery {
                q.let {
                    if (query != null && query.trim() != "" && query.isNotEmpty()) it.where {
                        ShardPagingEntityAlbum.title like "${query}%"
                    } else it
                }.orderBy(ShardPagingEntityAlbum.releaseYear to SortOrder.DESC)
                    .offset(if (page == 1) 0L else (page * size).toLong())
                    .limit(size)
                    .map {
                        DtoSearchItem(
                            id = it[ShardPagingEntityAlbum.id].value,
                            title = it[ShardPagingEntityAlbum.title],
                            rawPoster = it[ShardPagingEntityAlbum.poster],
                            releaseYear = it[ShardPagingEntityAlbum.releaseYear]
                        )
                    }
            }
        }
    }

    override suspend fun getPagingArtist(
        query: String?,
        page: Int,
        size: Int,
        filterType: DtoExploreArtistFilterType,
    ): List<DtoSearchItem> = shardSearchDbQuery {
        val all = ShardPagingEntityArtist.selectAll().let {
            if (query.isNullOrBlank().not()) it.where {
                ShardPagingEntityArtist.name like "$query%"
            } else it
        }

        when (filterType) {
            DtoExploreArtistFilterType.ALL -> all.orderBy(
                ShardPagingEntityArtist.name to SortOrder.ASC,
                ShardPagingEntityArtist.popularity to SortOrder.DESC
            )

            DtoExploreArtistFilterType.POPULARITY -> all.orderBy(ShardPagingEntityArtist.popularity to SortOrder.DESC)
        }.offset(if (page == 1) 0L else (page * size).toLong())
            .limit(size)
            .map {
                DtoSearchItem(
                    id = it[ShardPagingEntityArtist.id].value,
                    title = it[ShardPagingEntityArtist.name],
                    rawPoster = it[ShardPagingEntityArtist.cover],
                    isTypeSong = false,
                )
            }
    }

    override suspend fun getPagingSong(
        query: String?,
        page: Int,
        size: Int,
    ): List<DtoSearchItem> = shardSearchDbQuery {
        val aggregatedArtists = GroupConcat(
            expr = ShardPagingEntityArtist.name,
            separator = ", ",
            distinct = true
        ).alias("artists")

        ShardPagingEntitySong
            .join(
                otherTable = ShardPagingRelationEntityArtistSong,
                joinType = JoinType.INNER,
                onColumn = ShardPagingEntitySong.id,
                otherColumn = ShardPagingRelationEntityArtistSong.songId,
                additionalConstraint = {
                    ShardPagingEntitySong.id eq ShardPagingRelationEntityArtistSong.songId
                }
            ).join(
                otherTable = ShardPagingEntityArtist,
                joinType = JoinType.INNER,
                onColumn = ShardPagingRelationEntityArtistSong.artistId,
                otherColumn = ShardPagingEntityArtist.id,
                additionalConstraint = {
                    ShardPagingEntityArtist.id eq ShardPagingRelationEntityArtistSong.artistId
                }
            ).select(
                ShardPagingEntitySong.id,
                ShardPagingEntitySong.title,
                ShardPagingEntitySong.poster,
                ShardPagingEntityArtist.name,
                aggregatedArtists
            ).apply {
                if (!query.isNullOrBlank()) andWhere { ShardPagingEntitySong.title like "${query}%" }
            }.groupBy(
                ShardPagingEntitySong.id,
                ShardPagingEntitySong.title,
                ShardPagingEntitySong.poster,
                ShardPagingEntityArtist.name
            )
            .orderBy(ShardPagingEntitySong.title to SortOrder.ASC)
            .offset(if (page == 1) 0L else (page * size).toLong())
            .limit(size)
            .map {
                DtoSearchItem(
                    id = it[ShardPagingEntitySong.id].value,
                    title = it[ShardPagingEntitySong.title],
                    rawPoster = it[ShardPagingEntitySong.poster],
                    artist = it[aggregatedArtists],
                    isTypeSong = true
                )
            }
    }

    override suspend fun getPagingPlaylist(
        query: String?,
        page: Int,
        size: Int,
    ): List<DtoSearchItem> = coroutineScope {
        val playlist = kyokuDbQuery {
            DaoPlaylist.find {
                EntityPlaylist.isPublic eq true
            }.orderBy(EntityPlaylist.name to SortOrder.ASC)
                .offset(if (page == 1) 0L else (page * size).toLong())
                .limit(size).toList()
        }

        kyokuDbQuery {
            playlist.map {
                async {
                    DtoSearchItem(
                        id = it.id.value,
                        rawPosters = kyokuDbQuery {
                            RelationEntitySongPlaylist.select(RelationEntitySongPlaylist.songId).where {
                                RelationEntitySongPlaylist.playlistId eq it.id.value
                            }.limit(4).map { it[RelationEntitySongPlaylist.songId] }
                        }.let {
                            kyokuDbQuery { DaoSong.find { EntitySong.id inList it }.map { it.poster } }
                        },
                        title = it.name,
                        numbers = it.popularity
                    )
                }
            }.awaitAll()
        }
    }
}