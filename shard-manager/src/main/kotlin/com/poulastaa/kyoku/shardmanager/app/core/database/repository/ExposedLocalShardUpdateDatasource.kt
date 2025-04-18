package com.poulastaa.kyoku.shardmanager.app.core.database.repository

import com.poulastaa.kyoku.shardmanager.app.core.database.dao.kyoku.DaoAlbum
import com.poulastaa.kyoku.shardmanager.app.core.database.dao.kyoku.DaoArtist
import com.poulastaa.kyoku.shardmanager.app.core.database.dao.shard.genre_artist.ShardDaoArtist
import com.poulastaa.kyoku.shardmanager.app.core.database.dao.shard.suggestion.ShardDaoSong
import com.poulastaa.kyoku.shardmanager.app.core.database.entity.kyoku.*
import com.poulastaa.kyoku.shardmanager.app.core.database.entity.shard.genre_artist.ShardGenreArtistEntityArtist
import com.poulastaa.kyoku.shardmanager.app.core.database.entity.shard.genre_artist.ShardRelationEntityGenreTypeArtist
import com.poulastaa.kyoku.shardmanager.app.core.database.entity.shard.paging.*
import com.poulastaa.kyoku.shardmanager.app.core.database.entity.shard.suggestion.ShardEntityArtistPopularSong
import com.poulastaa.kyoku.shardmanager.app.core.database.entity.shard.suggestion.ShardEntityCountryPopularSong
import com.poulastaa.kyoku.shardmanager.app.core.database.entity.shard.suggestion.ShardEntityYearPopularSong
import com.poulastaa.kyoku.shardmanager.app.core.database.entity.shard.suggestion.ShardSuggestionEntitySong
import com.poulastaa.kyoku.shardmanager.app.core.database.model.*
import com.poulastaa.kyoku.shardmanager.app.core.database.toDbArtistDto
import com.poulastaa.kyoku.shardmanager.app.core.database.toDtoAlbum
import com.poulastaa.kyoku.shardmanager.app.core.domain.model.DtoDBArtist
import com.poulastaa.kyoku.shardmanager.app.core.domain.model.DtoDbSong
import com.poulastaa.kyoku.shardmanager.app.core.domain.repository.Genre
import com.poulastaa.kyoku.shardmanager.app.core.domain.repository.LocalShardUpdateDatasource
import com.poulastaa.kyoku.shardmanager.app.core.domain.utils.CURRENT_TIME
import com.poulastaa.kyoku.shardmanager.app.plugins.kyokuDbQuery
import com.poulastaa.kyoku.shardmanager.app.plugins.shardGenreArtistDbQuery
import com.poulastaa.kyoku.shardmanager.app.plugins.shardPopularDbQuery
import com.poulastaa.kyoku.shardmanager.app.plugins.shardSearchDbQuery
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList

private const val COUNTRY_MOST_POPULAR_SONGS_LIMIT = 6L
private const val YEAR_MOST_POPULAR_SONGS_LIMIT = 5L
private const val ARTIST_MOST_POPULAR_SONGS_LIMIT = 15L

class ExposedLocalShardUpdateDatasource private constructor() : LocalShardUpdateDatasource {
    override suspend fun isGenreArtistShardDatabasePopulated(): Boolean = shardGenreArtistDbQuery {
        ShardGenreArtistEntityArtist.exists() || try {
            ShardGenreArtistEntityArtist.selectAll().count() > 0
        } catch (_: Exception) {
            false
        }
    }

    override suspend fun isSuggestionShardDatabasePopulated(): Boolean =
        shardPopularDbQuery { ShardSuggestionEntitySong.selectAll().count() > 0 }

    override suspend fun isPagingShardDatabasePopulated(): Boolean = shardSearchDbQuery {
        ShardPagingEntityAlbum.selectAll().count() > 0 && ShardPagingEntityArtist.selectAll().count() > 0
                && ShardPagingRelationEntityArtistSong.selectAll().count() > 0
                && ShardPagingRelationEntityArtistAlbum.selectAll().count() > 0
    }

    // database genre artist shard
    override suspend fun getAllArtist(): List<DtoDBArtist> = kyokuDbQuery { DaoArtist.all().map { it.toDbArtistDto() } }
    override suspend fun getShardGenreArtistRelation(): List<DtoGenreArtistRelation> = kyokuDbQuery {
        EntityArtist
            .join(
                RelationEntityArtistGenre,
                JoinType.INNER,
                EntityArtist.id,
                RelationEntityArtistGenre.artistId
            )
            .join(EntityGenre, JoinType.INNER, RelationEntityArtistGenre.genreId, EntityGenre.id)
            .select(
                EntityArtist.popularity,
                RelationEntityArtistGenre.artistId,
                RelationEntityArtistGenre.genreId,
                EntityGenre.genre
            )
            .orderBy(EntityArtist.popularity, SortOrder.DESC)
            .map {
                DtoGenreArtistRelation(
                    genreId = it[RelationEntityArtistGenre.genreId],
                    artistId = it[RelationEntityArtistGenre.artistId],
                    popularity = it[EntityArtist.popularity],
                    genre = it[EntityGenre.genre]
                )
            }
    }

    override suspend fun createShardArtistTable() {
        shardGenreArtistDbQuery {
            try {
                SchemaUtils.create(ShardGenreArtistEntityArtist)
            } catch (e: Exception) {
                if (!e.message?.contains("Duplicate key name")!!) throw e
            }
        }
    }

    override suspend fun insertIntoShardArtist(data: List<DtoDBArtist>) {
        shardGenreArtistDbQuery {
            println("$CURRENT_TIME Inserting ${data.size} artists")

            ShardGenreArtistEntityArtist.batchInsert(data, ignore = true, shouldReturnGeneratedValues = false) {
                this[ShardGenreArtistEntityArtist.id] = it.id
                this[ShardGenreArtistEntityArtist.name] = it.name
                this[ShardGenreArtistEntityArtist.coverImage] = it.coverImage
                this[ShardGenreArtistEntityArtist.popularity] = it.popularity
            }
        }
    }

    override suspend fun insertIntoShardGenreArtistRelation(data: Map<String, List<DtoGenreArtistRelation>>) {
        data.map { (genre, relation) ->
            shardGenreArtistDbQuery {
                val table = getShardRelationEntityGenreTypeArtist(genre)

                table.batchInsert(relation, ignore = true, shouldReturnGeneratedValues = false) {
                    this[table.genreId] = it.genreId
                    this[table.artistId] = it.artistId
                    this[table.popularity] = it.popularity
                }
            }
        }
    }

    override suspend fun upsertSongPopularity() {
        coroutineScope {
            val song = kyokuDbQuery {
                EntitySongInfo.selectAll().map {
                    DtoDbSong(
                        id = it[EntitySongInfo.songId].value,
                        popularity = it[EntitySongInfo.popularity]
                    )
                }
            }

            song.chunked(5000).map { list ->
                async {
                    list.map { dto ->
                        val dao = shardPopularDbQuery { ShardDaoSong.findById(dto.id) }

                        if (dao != null) if (dao.popularity != dto.popularity) shardPopularDbQuery {
                            dao.popularity = dto.popularity
                        } else shardPopularDbQuery {
                            ShardSuggestionEntitySong.insertIgnore {
                                it[this.id] = dto.id
                                it[this.popularity] = dto.popularity
                            }
                        }
                    }
                }
            }.awaitAll()
        }
    }

    override suspend fun upsertArtistPopularity() {
        coroutineScope {
            val artist = kyokuDbQuery { DaoArtist.all().map { it.toDbArtistDto() } }

            artist.chunked(3000).map { list ->
                async {
                    list.map { dto ->
                        val dao = shardGenreArtistDbQuery { ShardDaoArtist.findById(dto.id) }

                        if (dao != null) {
                            if (dao.popularity != dao.popularity) shardPopularDbQuery {
                                dao.popularity = dto.popularity
                            }
                        } else shardGenreArtistDbQuery {
                            ShardGenreArtistEntityArtist.insertIgnore {
                                it[this.id] = dto.id
                                it[this.name] = dto.name
                                it[this.coverImage] = dto.coverImage
                                it[this.popularity] = dto.popularity
                            }
                        }
                    }
                }
            }.awaitAll()
        }
    }

    override suspend fun updateShardGenreArtistsRelation() {
        coroutineScope {
            getShardGenreArtistRelation().map { dto ->
                async {
                    shardGenreArtistDbQuery {
                        val table = getShardRelationEntityGenreTypeArtist(dto.genre)

                        val entry = table.selectAll().where {
                            table.genreId eq dto.genreId and (table.artistId eq dto.artistId)
                        }.firstOrNull()?.let {
                            DtoGenreArtistRelation(
                                genreId = it[table.genreId],
                                artistId = it[table.artistId],
                                popularity = it[table.popularity],
                                genre = dto.genre
                            )
                        }

                        if (entry == null) insertIntoShardGenreArtistRelation(mapOf(dto.genre to listOf(dto)))
                        else if (entry.popularity != dto.popularity) table.update(
                            where = {
                                table.genreId eq dto.genreId and (table.artistId eq dto.artistId)
                            },
                            body = {
                                it[table.popularity] = dto.popularity
                            }
                        )
                    }
                }
            }.awaitAll()
        }
    }

    // database suggestion shard
    override suspend fun getSongIdWithPopularity(): List<Pair<Long, Long>> = kyokuDbQuery {
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
            .select(
                EntitySong.id,
                EntitySongInfo.popularity
            ).map {
                it[EntitySong.id].value to it[EntitySongInfo.popularity]
            }
    }

    override suspend fun insertShardSongs(data: List<Pair<Long, Long>>) {
        shardPopularDbQuery {
            ShardSuggestionEntitySong.batchInsert(
                data = data,
                ignore = true,
                shouldReturnGeneratedValues = false
            ) {
                this[ShardSuggestionEntitySong.id] = it.first
                this[ShardSuggestionEntitySong.popularity] = it.second
            }
        }
    }

    override suspend fun insertCountrysMostPopularSongs() {
        getCountrysMostPopularSongs().let {
            println("$CURRENT_TIME Inserting ${it.size} country popular songs")

            shardPopularDbQuery {
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
    }

    override suspend fun insertYearMostPopularSongs() {
        getYearMostPopularSongs().let {
            println("$CURRENT_TIME Inserting ${it.size} year popular songs")

            shardPopularDbQuery {
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

    override suspend fun insertArtistsMostPopularSongs() {
        val rank = kyokuDbQuery {
            RowNumber()
                .over()
                .partitionBy(RelationEntitySongArtist.artistId)
                .orderBy(EntitySongInfo.popularity, SortOrder.DESC)
                .alias("rankk")
        }

        val rankedQuery = insertArtistsMostPopularSongsQuery(rank)

        kyokuDbQuery {
            rankedQuery.selectAll().where {
                (rankedQuery[rank]).lessEq(longLiteral(ARTIST_MOST_POPULAR_SONGS_LIMIT))
            }
                .orderBy(rankedQuery[EntitySongInfo.popularity] to SortOrder.DESC)
                .map {
                    DtoArtistPopularSong(
                        songId = it[rankedQuery.fields[0]].toString().toLong(),
                        artistId = it[rankedQuery.fields[1]].toString().toLong(),
                        countryId = it[rankedQuery.fields[2]].toString().toInt()
                    )
                }
        }.chunked(5000).map {
            println("$CURRENT_TIME Inserting ${it.size} artist popular songs")

            shardPopularDbQuery {
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

    private suspend fun insertArtistsMostPopularSongsQuery(rank: ExpressionWithColumnTypeAlias<Long>): QueryAlias =
        kyokuDbQuery {
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
                .select(
                    RelationEntitySongArtist.songId,
                    RelationEntitySongArtist.artistId,
                    RelationEntityArtistCountry.countryId,
                    EntityArtist.popularity,
                    rank
                )
                .alias("RankedSongs")
        }

    override suspend fun updateCountryMostPopularSongs() {
        coroutineScope {
            getCountrysMostPopularSongs()
                .groupBy { it.countryId }
                .map {
                    it.key to it.value.map { it.songId }
                }.map { (countryId, list) ->
                    val entrys = shardPopularDbQuery {
                        ShardEntityCountryPopularSong.selectAll().where {
                            ShardEntityCountryPopularSong.countryId eq countryId
                        }.map {
                            it[ShardEntityCountryPopularSong.countryId] to it[ShardEntityCountryPopularSong.id].value
                        }
                    }.groupBy { it.first }.map {
                        it.key to it.value.map { it.second }
                    }.firstOrNull()

                    if (entrys == null) shardPopularDbQuery {
                        ShardEntityArtistPopularSong.batchInsert(
                            data = list,
                            ignore = true,
                            shouldReturnGeneratedValues = false
                        ) {
                            this[ShardEntityCountryPopularSong.id] = it
                            this[ShardEntityCountryPopularSong.countryId] = countryId
                        }
                    } else {
                        val rev = entrys.second.filterNot { a -> list.any { it == a } }
                        val newEntry = list.filterNot { a -> entrys.second.any { it == a } }

                        val rem = async {
                            if (rev.isNotEmpty()) shardPopularDbQuery {
                                ShardEntityCountryPopularSong.deleteWhere {
                                    this.countryId eq countryId and (this.id inList rev)
                                }
                            }
                        }

                        val ins = async {
                            if (newEntry.isNotEmpty()) shardPopularDbQuery {
                                ShardEntityCountryPopularSong.batchInsert(
                                    data = newEntry,
                                    ignore = false,
                                    shouldReturnGeneratedValues = false
                                ) { data ->
                                    this[ShardEntityCountryPopularSong.countryId] = countryId
                                    this[ShardEntityCountryPopularSong.id] = data
                                }
                            }
                        }

                        listOf(
                            rem,
                            ins
                        ).awaitAll()
                    }
                }
        }
    }

    override suspend fun updateYearMostPopularSongs() {
        coroutineScope {
            getYearMostPopularSongs()
                .groupBy { it.year }
                .map {
                    it.key to it.value.map { it.songId }
                }.map { (year, list) ->
                    val entrys = shardPopularDbQuery {
                        ShardEntityYearPopularSong.selectAll().where {
                            ShardEntityYearPopularSong.year eq year
                        }.map {
                            it[ShardEntityYearPopularSong.year] to it[ShardEntityYearPopularSong.id].value
                        }
                    }.groupBy { it.first }.map {
                        it.key to it.value.map { it.second }
                    }.firstOrNull()

                    if (entrys == null) shardPopularDbQuery {
                        ShardEntityYearPopularSong.batchInsert(
                            data = list,
                            ignore = true,
                            shouldReturnGeneratedValues = false
                        ) {
                            this[ShardEntityYearPopularSong.id] = it
                            this[ShardEntityYearPopularSong.year] = year
                        }
                    } else {
                        val rev = entrys.second.filterNot { a -> list.any { it == a } }
                        val newEntry = list.filterNot { a -> entrys.second.any { it == a } }

                        val rem = async {
                            if (rev.isNotEmpty()) shardPopularDbQuery {
                                ShardEntityYearPopularSong.deleteWhere {
                                    this.year eq year and (this.id inList rev)
                                }
                            }
                        }

                        val ins = async {
                            if (newEntry.isNotEmpty()) shardPopularDbQuery {
                                ShardEntityYearPopularSong.batchInsert(
                                    data = newEntry,
                                    ignore = false,
                                    shouldReturnGeneratedValues = false
                                ) { data ->
                                    this[ShardEntityYearPopularSong.year] = year
                                    this[ShardEntityYearPopularSong.id] = data
                                }
                            }
                        }

                        listOf(rem, ins).awaitAll()
                    }
                }
        }
    }

    override suspend fun updateArtistMostPopularSongs() {
        val chunkSize = 5000
        var offset = 0L
        coroutineScope {
            val rank = kyokuDbQuery {
                RowNumber()
                    .over()
                    .partitionBy(RelationEntitySongArtist.artistId)
                    .orderBy(EntitySongInfo.popularity, SortOrder.DESC)
                    .alias("rankk")
            }

            val query = insertArtistsMostPopularSongsQuery(rank)

            while (true) {
                val chunk = kyokuDbQuery {
                    query.selectAll()
                        .where {
                            (query[rank]).lessEq(longLiteral(ARTIST_MOST_POPULAR_SONGS_LIMIT))
                        }.orderBy(query[EntitySongInfo.popularity] to SortOrder.DESC)
                        .limit(chunkSize)
                        .offset(offset).map {
                            DtoArtistPopularSong(
                                songId = it[query.fields[0]].toString().toLong(),
                                artistId = it[query.fields[1]].toString().toLong(),
                                countryId = it[query.fields[2]].toString().toInt()
                            )
                        }
                }

                if (chunk.isEmpty()) break
                else {
                    chunk.groupBy { it.artistId }.map {
                        val artistId = it.key
                        val countryId = it.value.first().countryId
                        val songIdList = it.value.map { it.songId }

                        val entry = shardPopularDbQuery {
                            ShardEntityArtistPopularSong.select(ShardEntityArtistPopularSong.id).where {
                                ShardEntityArtistPopularSong.artistId eq artistId and
                                        (ShardEntityArtistPopularSong.countryId eq countryId)
                            }.map {
                                it[ShardEntityArtistPopularSong.id].value
                            }
                        }

                        val rev = entry.filterNot { a -> songIdList.any { it == a } }
                        val newEntry = songIdList.filterNot { a -> entry.any { it == a } }

                        val rem = async {
                            if (rev.isNotEmpty()) shardPopularDbQuery {
                                ShardEntityArtistPopularSong.deleteWhere {
                                    this.artistId eq artistId and (this.countryId eq countryId) and (this.id inList rev)
                                }
                            }
                        }
                        val ins = async {
                            if (newEntry.isNotEmpty()) shardPopularDbQuery {
                                ShardEntityArtistPopularSong.batchInsert(
                                    data = newEntry,
                                    ignore = false,
                                    shouldReturnGeneratedValues = false,
                                    body = {
                                        this[ShardEntityArtistPopularSong.id] = it
                                        this[ShardEntityArtistPopularSong.countryId] = countryId
                                        this[ShardEntityArtistPopularSong.artistId] = artistId
                                    }
                                )
                            }
                        }

                        listOf(rem, ins).awaitAll()
                    }

                    offset += chunkSize
                }
            }
        }
    }

    override suspend fun getAllSongs(): List<DtoShardPagingSong> = kyokuDbQuery {
        EntitySong.join(
            otherTable = EntitySongInfo,
            joinType = JoinType.INNER,
            onColumn = EntitySong.id,
            otherColumn = EntitySongInfo.songId,
            additionalConstraint = {
                EntitySong.id eq EntitySongInfo.songId
            }
        ).select(
            EntitySong.id,
            EntitySong.title,
            EntitySong.poster,
            EntitySongInfo.releaseYear
        ).map {
            DtoShardPagingSong(
                id = it[EntitySong.id].value,
                title = it[EntitySong.title],
                poster = it[EntitySong.poster],
                releaseYear = it[EntitySongInfo.releaseYear]
            )
        }
    }

    override suspend fun getAllAlbums(): List<DtoAlbum> = coroutineScope {
        kyokuDbQuery {
            DaoAlbum.all().chunked(4000).map { list ->
                async {
                    val idList = list.map { it.id.value }

                    kyokuDbQuery {
                        EntityAlbum.join(
                            otherTable = RelationEntitySongAlbum,
                            joinType = JoinType.INNER,
                            onColumn = EntityAlbum.id,
                            otherColumn = RelationEntitySongAlbum.albumId,
                            additionalConstraint = {
                                EntityAlbum.id eq RelationEntitySongAlbum.albumId
                            }
                        ).join(
                            otherTable = EntitySongInfo,
                            joinType = JoinType.INNER,
                            onColumn = RelationEntitySongAlbum.songId,
                            otherColumn = EntitySongInfo.songId,
                            additionalConstraint = {
                                RelationEntitySongAlbum.songId eq EntitySongInfo.songId
                            }
                        ).join(
                            otherTable = EntitySong,
                            joinType = JoinType.INNER,
                            onColumn = RelationEntitySongAlbum.songId,
                            otherColumn = EntitySong.id,
                            additionalConstraint = {
                                RelationEntitySongAlbum.songId eq EntitySong.id
                            }
                        ).select(
                            EntitySongInfo.releaseYear,
                            EntitySong.poster,
                            EntityAlbum.id
                        ).where {
                            EntityAlbum.id inList idList
                        }.associate {
                            it[EntityAlbum.id].value to Pair(it[EntitySongInfo.releaseYear], it[EntitySong.poster])
                        }
                    }.let {
                        list.map { dto ->
                            if (it.keys.contains(dto.id.value)) dto.toDtoAlbum(
                                releaseYear = it[dto.id.value]!!.first,
                                poster = it[dto.id.value]?.second
                            ) else null
                        }.mapNotNull { it }
                    }
                }
            }.awaitAll().flatten()
        }
    }

    override suspend fun getArtistAlbumRelation(): List<DtoShardPagingArtistAlbumRelation> = kyokuDbQuery {
        EntitySong
            .join(
                otherTable = RelationEntitySongAlbum,
                joinType = JoinType.INNER,
                onColumn = RelationEntitySongAlbum.songId,
                otherColumn = EntitySong.id,
                additionalConstraint = {
                    EntitySong.id eq RelationEntitySongAlbum.songId
                }
            ).join(
                otherTable = RelationEntitySongArtist,
                joinType = JoinType.INNER,
                onColumn = RelationEntitySongArtist.songId,
                otherColumn = EntitySong.id,
                additionalConstraint = {
                    EntitySong.id eq RelationEntitySongArtist.songId
                }
            ).join(
                otherTable = EntityArtist,
                joinType = JoinType.INNER,
                onColumn = RelationEntitySongArtist.artistId,
                otherColumn = EntityArtist.id,
                additionalConstraint = {
                    RelationEntitySongArtist.artistId eq EntityArtist.id
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
                EntityAlbum.id,
                EntityArtist.id
            ).map {
                DtoShardPagingArtistAlbumRelation(
                    artistId = it[EntityArtist.id].value,
                    albumId = it[EntityAlbum.id].value,
                )
            }
    }

    override suspend fun getArtistSongRelation(): List<DtoShardPagingArtistSongRelation> = kyokuDbQuery {
        RelationEntitySongArtist.selectAll().map {
            DtoShardPagingArtistSongRelation(
                artistId = it[RelationEntitySongArtist.artistId],
                songId = it[RelationEntitySongArtist.songId],
            )
        }
    }

    override suspend fun insertShardPagingEntityAlbum(list: List<DtoAlbum>) {
        shardSearchDbQuery {
            ShardPagingEntityAlbum.batchInsert(
                data = list,
                ignore = true,
                shouldReturnGeneratedValues = false,
                body = {
                    this[ShardPagingEntityAlbum.id] = it.id
                    this[ShardPagingEntityAlbum.title] = it.name
                    this[ShardPagingEntityAlbum.poster] = it.poster
                    this[ShardPagingEntityAlbum.popularity] = it.popularity
                    this[ShardPagingEntityAlbum.releaseYear] = it.releaseYear
                }
            )
        }
    }

    override suspend fun insertShardPagingEntityArtist(list: List<DtoDBArtist>) {
        shardSearchDbQuery {
            ShardPagingEntityArtist.batchInsert(
                data = list,
                ignore = true,
                shouldReturnGeneratedValues = false,
                body = {
                    this[ShardPagingEntityArtist.id] = it.id
                    this[ShardPagingEntityArtist.name] = it.name
                    this[ShardPagingEntityArtist.cover] = it.coverImage
                    this[ShardPagingEntityArtist.popularity] = it.popularity
                }
            )
        }
    }

    override suspend fun insertShardPagingEntitySong(list: List<DtoShardPagingSong>) {
        shardSearchDbQuery {
            ShardPagingEntitySong.batchInsert(
                data = list,
                ignore = true,
                shouldReturnGeneratedValues = false,
                body = {
                    this[ShardPagingEntitySong.id] = it.id
                    this[ShardPagingEntitySong.title] = it.title
                    this[ShardPagingEntitySong.poster] = it.poster
                    this[ShardPagingEntitySong.releaseYear] = it.releaseYear
                }
            )
        }
    }

    override suspend fun insertArtistAlbumRelation(list: List<DtoShardPagingArtistAlbumRelation>) {
        shardSearchDbQuery {
            ShardPagingRelationEntityArtistAlbum.batchInsert(
                data = list,
                ignore = true,
                shouldReturnGeneratedValues = false,
                body = {
                    this[ShardPagingRelationEntityArtistAlbum.albumId] = it.albumId
                    this[ShardPagingRelationEntityArtistAlbum.artistId] = it.artistId
                }
            )
        }
    }

    override suspend fun insertArtistSongRelation(list: List<DtoShardPagingArtistSongRelation>) {
        shardSearchDbQuery {
            ShardPagingRelationEntityArtistSong.batchInsert(
                data = list,
                ignore = true,
                shouldReturnGeneratedValues = false,
                body = {
                    this[ShardPagingRelationEntityArtistSong.songId] = it.songId
                    this[ShardPagingRelationEntityArtistSong.artistId] = it.artistId
                }
            )
        }
    }


    private suspend fun getCountrysMostPopularSongs(): List<DtoCountryPopularSong> {
        val songId = EntitySong.id.alias("songId")
        val popularity = EntitySongInfo.popularity.alias("popularity")
        val countryId = EntityCountry.id.alias("countryId")

        val uniqueSongs = kyokuDbQuery {
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
                .select(songId, popularity, countryId)
                .withDistinct(true)
                .alias("UniqueSongs")
        }
        val rank = kyokuDbQuery {
            RowNumber()
                .over()
                .partitionBy(uniqueSongs[countryId])
                .orderBy(uniqueSongs[popularity], SortOrder.DESC)
                .alias("`rank`")
        }
        val rankedSongs = kyokuDbQuery {
            uniqueSongs.select(
                uniqueSongs.fields[0],
                uniqueSongs.fields[1],
                uniqueSongs.fields[2],
                rank
            ).alias("RankedSong")
        }

        return kyokuDbQuery {
            rankedSongs
                .select(
                    rankedSongs.fields[0],
                    rankedSongs.fields[1],
                    rankedSongs.fields[2]
                )
                .where { rankedSongs[rank] lessEq longLiteral(COUNTRY_MOST_POPULAR_SONGS_LIMIT) }
                .orderBy(
                    rankedSongs.fields[2] to SortOrder.DESC,
                    rankedSongs.fields[1] to SortOrder.DESC,
                ).map {
                    DtoCountryPopularSong(
                        songId = it[rankedSongs.fields[0]].toString().toLong(),
                        countryId = it[rankedSongs.fields[2]].toString().toInt(),
                    )
                }
        }
    }

    private suspend fun getYearMostPopularSongs(): List<DtoYearPopularSong> {
        val rank = RowNumber()
            .over()
            .partitionBy(EntitySongInfo.releaseYear)
            .orderBy(EntitySongInfo.popularity, SortOrder.DESC)
            .alias("rankk")

        val rankedQuery = kyokuDbQuery {
            EntitySongInfo
                .select(
                    EntitySongInfo.songId,
                    EntitySongInfo.releaseYear,
                    EntitySongInfo.popularity,
                    rank
                )
                .alias("RankedSongs")
        }

        return kyokuDbQuery {
            rankedQuery.selectAll().where {
                rankedQuery[rank].lessEq(longLiteral(YEAR_MOST_POPULAR_SONGS_LIMIT))
            }
                .orderBy(
                    rankedQuery[EntitySongInfo.releaseYear] to SortOrder.DESC,
                    rankedQuery[EntitySongInfo.popularity] to SortOrder.DESC
                )
                .map {
                    DtoYearPopularSong(
                        songId = it[rankedQuery.fields[0]].toString().toLong(),
                        year = it[rankedQuery.fields[1]].toString().toInt(),
                    )
                }
        }
    }

    private suspend fun getShardRelationEntityGenreTypeArtist(genre: Genre) = shardGenreArtistDbQuery {
        val table = ShardRelationEntityGenreTypeArtist(genre)

        if (table.exists().not()) try {
            SchemaUtils.create(ShardRelationEntityGenreTypeArtist(genre))
        } catch (_: Exception) {
        }

        table
    }

    companion object {
        @Volatile
        private var INSTANCE: LocalShardUpdateDatasource? = null

        fun instance(): LocalShardUpdateDatasource = INSTANCE ?: synchronized(this) {
            INSTANCE ?: ExposedLocalShardUpdateDatasource().also { INSTANCE = it }
        }
    }
}