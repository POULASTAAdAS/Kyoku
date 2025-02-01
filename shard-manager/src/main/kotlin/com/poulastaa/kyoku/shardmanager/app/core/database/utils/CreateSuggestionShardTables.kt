package com.poulastaa.kyoku.shardmanager.app.core.database.utils

import com.poulastaa.kyoku.shardmanager.app.core.database.entity.kyoku.*
import com.poulastaa.kyoku.shardmanager.app.core.database.entity.shard.suggestion.ShardEntityCountryPopularSong
import com.poulastaa.kyoku.shardmanager.app.core.database.entity.shard.suggestion.ShardEntitySong
import com.poulastaa.kyoku.shardmanager.app.core.database.model.DtoCountryPopularSong
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

        // insert country popular song
        val countryPopularSongDef = async {
            val slice = kyokuDbQuery {
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
                        slice
                    )
                    .selectAll()
                    .alias("RankedSongs")
            }

            kyokuDbQuery {
                rankedQuery
                    .select {
                        (rankedQuery[slice] as Column<*>).lessEq(4)
                    }
                    .orderBy(rankedQuery[EntityCountry.id] to SortOrder.DESC)
                    .orderBy(rankedQuery[EntitySongInfo.popularity] to SortOrder.DESC)
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
        val artistPopularSong = async {

        }
    }
}
