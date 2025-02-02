package com.poulastaa.kyoku.shardmanager.app.core.database.utils

import com.poulastaa.kyoku.shardmanager.app.core.database.entity.kyoku.*
import com.poulastaa.kyoku.shardmanager.app.core.database.entity.shard.suggestion.ShardEntityArtistPopularSong
import com.poulastaa.kyoku.shardmanager.app.core.database.entity.shard.suggestion.ShardEntityCountryPopularSong
import com.poulastaa.kyoku.shardmanager.app.core.database.entity.shard.suggestion.ShardEntitySong
import com.poulastaa.kyoku.shardmanager.app.core.database.entity.shard.suggestion.ShardEntityYearPopularSong
import com.poulastaa.kyoku.shardmanager.app.core.database.model.DtoArtistPopularSong
import com.poulastaa.kyoku.shardmanager.app.core.database.model.DtoCountryPopularSong
import com.poulastaa.kyoku.shardmanager.app.core.database.model.DtoYearPopularSong
import com.poulastaa.kyoku.shardmanager.app.plugins.kyokuDbQuery
import com.poulastaa.kyoku.shardmanager.app.plugins.popularDbQuery
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*

fun createSuggestionShardTables() = runBlocking {
    coroutineScope {
        // insert song
        kyokuDbQuery {
            EntitySong
                .join(
                    otherTable = EntitySongInfo,
                    joinType = JoinType.INNER,
                    onColumn = EntitySong.id,
                    otherColumn = EntitySongInfo.songId,
                    additionalConstraint = {
                        EntitySong.id eq EntitySongInfo.songId
                    }
                )
                .slice(
                    EntitySong.id,
                    EntitySongInfo.popularity
                )
                .selectAll().map {
                    it[EntitySong.id].value to it[EntitySongInfo.popularity]
                }
        }.chunked(10000).map {
            async {
                popularDbQuery {
                    ShardEntitySong.batchInsert(
                        data = it,
                        ignore = true,
                        shouldReturnGeneratedValues = false
                    ) {
                        this[ShardEntitySong.id] = it.first
                        this[ShardEntitySong.popularity] = it.second
                    }
                }
            }
        }.awaitAll()

        // insert country most popular song
        val countryPopularSongDef = async {
            val rank = kyokuDbQuery {
                RowNumber()
                    .over()
                    .partitionBy(EntityCountry.id)
                    .orderBy(EntitySongInfo.popularity, SortOrder.DESC)
                    .alias("rank")
            }

            val rankedQuery = kyokuDbQuery {
                EntitySong
                    .join(
                        otherTable = EntitySongInfo,
                        joinType = JoinType.INNER,
                        additionalConstraint = {
                            EntitySong.id eq EntitySongInfo.songId
                        }
                    )
                    .join(
                        otherTable = RelationEntitySongArtist,
                        joinType = JoinType.INNER,
                        additionalConstraint = {
                            RelationEntitySongArtist.songId eq EntitySong.id as Column<*>
                        }
                    )
                    .join(
                        otherTable = RelationEntityArtistCountry,
                        joinType = JoinType.INNER,
                        additionalConstraint = {
                            RelationEntityArtistCountry.artistId eq RelationEntitySongArtist.artistId
                        }
                    )
                    .join(
                        otherTable = EntityCountry,
                        joinType = JoinType.INNER,
                        additionalConstraint = {
                            RelationEntityArtistCountry.countryId eq EntityCountry.id as Column<*>
                        }
                    )
                    .slice(
                        EntitySong.id,
                        EntitySongInfo.popularity,
                        EntityCountry.id,
                        rank
                    )
                    .selectAll()
                    .alias("RankedSongs")
            }

            kyokuDbQuery {
                rankedQuery
                    .select {
                        (rankedQuery[rank]).lessEq(longLiteral(4))
                    }
                    .orderBy(
                        rankedQuery[EntityCountry.id] to SortOrder.DESC,
                        rankedQuery[EntitySongInfo.popularity] to SortOrder.DESC
                    )
                    .map {
                        DtoCountryPopularSong(
                            songId = it[EntitySong.id].value,
                            countryId = it[EntityCountry.id].value
                        )
                    }
            }.chunked(3000).map {
                async {
                    popularDbQuery {
                        ShardEntityCountryPopularSong.batchInsert(
                            data = it,
                            ignore = true,
                            shouldReturnGeneratedValues = false
                        ) {
                            this[ShardEntityCountryPopularSong.id] = it.songId
                            this[ShardEntityCountryPopularSong.countryId] = it.countryId
                        }
                    }
                }
            }.awaitAll()
        }

        // insert artist most popular
        val artistPopularSong = async {
            val rank = kyokuDbQuery {
                RowNumber()
                    .over()
                    .partitionBy(RelationEntitySongArtist.artistId)
                    .orderBy(EntitySongInfo.popularity, SortOrder.DESC)
                    .alias("rank")
            }

            val rankedQuery = kyokuDbQuery {
                RelationEntitySongArtist
                    .join(
                        otherTable = EntitySongInfo,
                        joinType = JoinType.INNER,
                        additionalConstraint = {
                            RelationEntitySongArtist.songId eq EntitySongInfo.songId as Column<*>
                        }
                    )
                    .join(
                        otherTable = RelationEntityArtistCountry,
                        joinType = JoinType.INNER,
                        additionalConstraint = {
                            RelationEntitySongArtist.artistId eq RelationEntityArtistCountry.artistId
                        }
                    )
                    .join(
                        otherTable = EntityArtist,
                        joinType = JoinType.INNER,
                        additionalConstraint = {
                            RelationEntityArtistCountry.artistId eq EntityArtist.id as Column<*>
                        }
                    )
                    .slice(
                        RelationEntitySongArtist.songId,
                        RelationEntitySongArtist.artistId,
                        RelationEntityArtistCountry.countryId,
                        EntityArtist.popularity,
                        rank
                    ).selectAll()
                    .alias("RankedSongs")
            }

            kyokuDbQuery {
                rankedQuery
                    .select {
                        (rankedQuery[rank]).lessEq(longLiteral(15))
                    }
                    .orderBy(rankedQuery[EntitySongInfo.popularity] to SortOrder.DESC)
                    .map {
                        DtoArtistPopularSong(
                            songId = it[RelationEntitySongArtist.songId],
                            artistId = it[RelationEntitySongArtist.artistId],
                            countryId = it[RelationEntityArtistCountry.countryId]
                        )
                    }
            }.chunked(5000).map {
                async {
                    popularDbQuery {
                        ShardEntityArtistPopularSong.batchInsert(
                            data = it,
                            ignore = true,
                            shouldReturnGeneratedValues = false
                        ) {
                            this[ShardEntityArtistPopularSong.id] = it.songId
                            this[ShardEntityArtistPopularSong.artistId] = it.artistId
                            this[ShardEntityArtistPopularSong.countryId] = it.countryId
                        }
                    }
                }
            }
        }

        // insert year most popular song
        val yearMostPopularSongDef = async {
            val rank = RowNumber()
                .over()
                .partitionBy(EntitySongInfo.releaseYear)
                .orderBy(EntitySongInfo.popularity, SortOrder.DESC)
                .alias("rank")

            val rankedQuery = kyokuDbQuery {
                EntitySongInfo
                    .slice(
                        EntitySongInfo.songId,
                        EntitySongInfo.releaseYear,
                        EntitySongInfo.popularity,
                        rank
                    )
                    .selectAll()
                    .alias("RankedSongs")
            }

            kyokuDbQuery {
                rankedQuery
                    .select {
                        rankedQuery[rank].lessEq(longLiteral(5))
                    }
                    .orderBy(
                        rankedQuery[EntitySongInfo.releaseYear] to SortOrder.DESC,
                        rankedQuery[EntitySongInfo.popularity] to SortOrder.DESC
                    )
                    .map {
                        DtoYearPopularSong(
                            songId = it[EntitySongInfo.songId].value,
                            year = it[EntitySongInfo.releaseYear]
                        )
                    }
            }.chunked(5000).map {
                async {
                    popularDbQuery {
                        ShardEntityYearPopularSong.batchInsert(
                            data = it,
                            ignore = true,
                            shouldReturnGeneratedValues = false
                        ) {
                            this[ShardEntityYearPopularSong.id] = it.songId
                            this[ShardEntityYearPopularSong.year] = it.year
                        }
                    }
                }
            }
        }

        listOf(
            countryPopularSongDef,
            artistPopularSong,
            yearMostPopularSongDef
        ).awaitAll()
    }
}
