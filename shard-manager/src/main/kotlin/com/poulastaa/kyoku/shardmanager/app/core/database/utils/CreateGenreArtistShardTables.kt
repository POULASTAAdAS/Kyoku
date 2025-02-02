package com.poulastaa.kyoku.shardmanager.app.core.database.utils

import com.poulastaa.kyoku.shardmanager.app.core.database.dao.kyoku.DaoArtist
import com.poulastaa.kyoku.shardmanager.app.core.database.entity.kyoku.EntityArtist
import com.poulastaa.kyoku.shardmanager.app.core.database.entity.kyoku.EntityGenre
import com.poulastaa.kyoku.shardmanager.app.core.database.entity.kyoku.RelationEntityArtistGenre
import com.poulastaa.kyoku.shardmanager.app.core.database.entity.shard.genre_artist.ShardEntityArtist
import com.poulastaa.kyoku.shardmanager.app.core.database.entity.shard.genre_artist.ShardRelationEntityGenreTypeArtist
import com.poulastaa.kyoku.shardmanager.app.core.database.model.DtoGenreArtistRelation
import com.poulastaa.kyoku.shardmanager.app.core.database.toDbArtistDto
import com.poulastaa.kyoku.shardmanager.app.plugins.kyokuDbQuery
import com.poulastaa.kyoku.shardmanager.app.plugins.shardGenreArtistDbQuery
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*

fun createGenreArtistShardTables() = runBlocking {
    val isPopulated = shardGenreArtistDbQuery {
        ShardEntityArtist.exists() || try {
            ShardEntityArtist.selectAll().count() > 0
        } catch (_: Exception) {
            false
        }
    }
    if (isPopulated) return@runBlocking println("Artist Shard Tables are populated")

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

            artistEntry.chunked(5000).map { entry ->
                println("Inserting ${entry.size} artists")

                ShardEntityArtist.batchInsert(entry, ignore = true, shouldReturnGeneratedValues = false) {
                    this[ShardEntityArtist.id] = it.id
                    this[ShardEntityArtist.name] = it.name
                    this[ShardEntityArtist.coverImage] = it.coverImage
                    this[ShardEntityArtist.popularity] = it.popularity
                }
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
        }.awaitAll().also {
            println("Inserted Genres Most Popular Artists")
        }
    }
}
