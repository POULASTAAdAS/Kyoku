package com.poulastaa.kyoku.shardmanager.app.core.database.utils

import com.poulastaa.kyoku.shardmanager.app.core.database.dao.kyoku.DaoArtist
import com.poulastaa.kyoku.shardmanager.app.core.database.dao.shard.genre_artist.ShardDaoArtist
import com.poulastaa.kyoku.shardmanager.app.core.database.entity.kyoku.EntityArtist
import com.poulastaa.kyoku.shardmanager.app.core.database.entity.kyoku.EntityGenre
import com.poulastaa.kyoku.shardmanager.app.core.database.entity.kyoku.RelationEntityArtistGenre
import com.poulastaa.kyoku.shardmanager.app.core.database.entity.shard.genre_artist.ShardEntityArtist
import com.poulastaa.kyoku.shardmanager.app.core.database.entity.shard.genre_artist.ShardRelationEntityGenreTypeArtist
import com.poulastaa.kyoku.shardmanager.app.core.database.model.DtoGenreArtistRelation
import com.poulastaa.kyoku.shardmanager.app.core.database.toDbArtistDto
import com.poulastaa.kyoku.shardmanager.app.plugins.kyokuDbQuery
import com.poulastaa.kyoku.shardmanager.app.plugins.shardGenreArtistDbQuery
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*

fun createGenreArtistShardTables() = runBlocking {
    coroutineScope {
        val artistDeferred = async {
            kyokuDbQuery { DaoArtist.all().map { it.toDbArtistDto() } }
        }
        val genreArtistDeferred = async {
            kyokuDbQuery {
                EntityArtist
                    .join(
                        RelationEntityArtistGenre,
                        JoinType.INNER,
                        EntityArtist.id,
                        RelationEntityArtistGenre.artistId
                    )
                    .join(EntityGenre, JoinType.INNER, RelationEntityArtistGenre.genreId, EntityGenre.id)
                    .slice(
                        EntityArtist.popularity,
                        RelationEntityArtistGenre.artistId,
                        RelationEntityArtistGenre.genreId,
                        EntityGenre.genre
                    )
                    .selectAll()
                    .orderBy(EntityArtist.popularity, SortOrder.DESC)
                    .map {
                        DtoGenreArtistRelation(
                            genreId = it[RelationEntityArtistGenre.genreId],
                            artistId = it[RelationEntityArtistGenre.artistId],
                            popularity = it[EntityArtist.popularity],
                            genre = it[EntityGenre.genre]
                        )
                    }
            }.groupBy { it.genre }
        }

        val artistEntry = artistDeferred.await()
        val genreArtist = genreArtistDeferred.await()

        shardGenreArtistDbQuery {
            try {
                SchemaUtils.create(ShardEntityArtist)
            } catch (e: Exception) {
                if (!e.message?.contains("Duplicate key name")!!) throw e
            }

            ShardEntityArtist.batchInsert(artistEntry, ignore = true, shouldReturnGeneratedValues = false) {
                this[ShardEntityArtist.id] = it.id
                this[ShardEntityArtist.name] = it.name
                this[ShardEntityArtist.coverImage] = it.coverImage
                this[ShardEntityArtist.popularity] = it.popularity
            }
        }
        genreArtist.map { (genre, relation) ->
            async {
                shardGenreArtistDbQuery {
                    val table = ShardRelationEntityGenreTypeArtist(genre)
                    try {
                        SchemaUtils.create(ShardRelationEntityGenreTypeArtist(genre))
                    } catch (e: Exception) {
                        if (!e.message?.contains("Duplicate key name")!!) throw e
                    }

                    table.batchInsert(relation, ignore = true, shouldReturnGeneratedValues = false) {
                        this[table.genreId] = it.genreId
                        this[table.artistId] = it.artistId
                        this[table.popularity] = it.popularity
                    }
                }
            }
        }.awaitAll()
    }

    updateArtistPopularityOnceADay()
}

private fun updateArtistPopularityOnceADay() {
    CoroutineScope(Dispatchers.IO).launch {
        while (true) {
            delay(12 * 60 * 60 * 1000) // wait for 12 hours

            // update artist popularity
            val artist =
                kyokuDbQuery { DaoArtist.all().map { it.toDbArtistDto() } }

            artist.map { dto ->
                async {
                    shardGenreArtistDbQuery {
                        ShardDaoArtist.findById(dto.id)?.let { table ->
                            if (dto.popularity != table.popularity) table.popularity = dto.popularity
                        }
                    }
                }
            }.awaitAll()
        }
    }
}
