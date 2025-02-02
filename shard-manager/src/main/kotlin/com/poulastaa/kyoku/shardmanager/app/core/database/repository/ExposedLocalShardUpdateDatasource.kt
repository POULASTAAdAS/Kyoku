package com.poulastaa.kyoku.shardmanager.app.core.database.repository

import com.poulastaa.kyoku.shardmanager.app.core.database.dao.kyoku.DaoArtist
import com.poulastaa.kyoku.shardmanager.app.core.database.dao.shard.genre_artist.ShardDaoArtist
import com.poulastaa.kyoku.shardmanager.app.core.database.dao.shard.suggestion.ShardDaoSong
import com.poulastaa.kyoku.shardmanager.app.core.database.entity.kyoku.*
import com.poulastaa.kyoku.shardmanager.app.core.database.entity.shard.genre_artist.ShardEntityArtist
import com.poulastaa.kyoku.shardmanager.app.core.database.entity.shard.genre_artist.ShardRelationEntityGenreTypeArtist
import com.poulastaa.kyoku.shardmanager.app.core.database.entity.shard.suggestion.ShardEntityArtistPopularSong
import com.poulastaa.kyoku.shardmanager.app.core.database.entity.shard.suggestion.ShardEntityCountryPopularSong
import com.poulastaa.kyoku.shardmanager.app.core.database.entity.shard.suggestion.ShardEntitySong
import com.poulastaa.kyoku.shardmanager.app.core.database.entity.shard.suggestion.ShardEntityYearPopularSong
import com.poulastaa.kyoku.shardmanager.app.core.database.model.DtoArtistPopularSong
import com.poulastaa.kyoku.shardmanager.app.core.database.model.DtoCountryPopularSong
import com.poulastaa.kyoku.shardmanager.app.core.database.model.DtoGenreArtistRelation
import com.poulastaa.kyoku.shardmanager.app.core.database.model.DtoYearPopularSong
import com.poulastaa.kyoku.shardmanager.app.core.database.toDbArtistDto
import com.poulastaa.kyoku.shardmanager.app.core.domain.model.DtoDBArtist
import com.poulastaa.kyoku.shardmanager.app.core.domain.repository.Genre
import com.poulastaa.kyoku.shardmanager.app.core.domain.repository.LocalShardUpdateDatasource
import com.poulastaa.kyoku.shardmanager.app.plugins.kyokuDbQuery
import com.poulastaa.kyoku.shardmanager.app.plugins.shardGenreArtistDbQuery
import com.poulastaa.kyoku.shardmanager.app.plugins.shardPopularDbQuery
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList

private const val COUNTRY_MOST_POPULAR_SONGS_LIMIT = 4L
private const val YEAR_MOST_POPULAR_SONGS_LIMIT = 5L
private const val ARTIST_MOST_POPULAR_SONGS_LIMIT = 15L

class ExposedLocalShardUpdateDatasource private constructor() : LocalShardUpdateDatasource {
    override suspend fun isGenreArtistShardDatabasePopulated(): Boolean = shardGenreArtistDbQuery {
        ShardEntityArtist.exists() || try {
            ShardEntityArtist.selectAll().count() > 0
        } catch (_: Exception) {
            false
        }
    }

    override suspend fun isSuggestionShardDatabasePopulated(): Boolean =
        shardPopularDbQuery { ShardEntitySong.selectAll().count() > 0 }

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
                SchemaUtils.create(ShardEntityArtist)
            } catch (e: Exception) {
                if (!e.message?.contains("Duplicate key name")!!) throw e
            }
        }
    }

    override suspend fun insertIntoShardArtist(data: List<DtoDBArtist>) {
        shardGenreArtistDbQuery {
            println("Inserting ${data.size} artists")

            ShardEntityArtist.batchInsert(data, ignore = true, shouldReturnGeneratedValues = false) {
                this[ShardEntityArtist.id] = it.id
                this[ShardEntityArtist.name] = it.name
                this[ShardEntityArtist.coverImage] = it.coverImage
                this[ShardEntityArtist.popularity] = it.popularity
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
            val song = kyokuDbQuery { DaoArtist.all().map { it.toDbArtistDto() } }

            shardPopularDbQuery {
                song.chunked(5000).map { list ->
                    async {
                        list.map { dto ->
                            val dao = ShardDaoSong.findById(dto.id)

                            if (dao != null) if (dao.popularity != dto.popularity) dao.popularity = dto.popularity
                            else shardPopularDbQuery {
                                ShardDaoSong.new(dto.id) {
                                    this.popularity = dto.popularity
                                }
                            }
                        }
                    }
                }.awaitAll()
            }
        }
    }

    override suspend fun upsertArtistPopularity() {
        coroutineScope {
            val artist = kyokuDbQuery { DaoArtist.all().map { it.toDbArtistDto() } }

            shardGenreArtistDbQuery {
                artist.chunked(3000).map { list ->
                    async {
                        list.map { dto ->
                            val dao = ShardDaoArtist.findById(dto.id)

                            if (dao != null) if (dao.popularity != dao.popularity) dao.popularity = dto.popularity
                            else shardGenreArtistDbQuery {
                                ShardDaoArtist.new(dto.id) {
                                    this.popularity = dto.popularity
                                    this.name = dto.name
                                    this.coverImage = dto.coverImage
                                }
                            }
                        }
                    }
                }.awaitAll()
            }
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
    override suspend fun getSongs(): List<Pair<Long, Long>> = kyokuDbQuery {
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
            ShardEntitySong.batchInsert(
                data = data,
                ignore = true,
                shouldReturnGeneratedValues = false
            ) {
                this[ShardEntitySong.id] = it.first
                this[ShardEntitySong.popularity] = it.second
            }
        }
    }

    override suspend fun insertCountrysMostPopularSongs() {
        getCountrysMostPopularSongs().let {
            println("Inserting ${it.size} country popular songs")

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
            println("Inserting ${it.size} year popular songs")

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
                .select(
                    RelationEntitySongArtist.songId,
                    RelationEntitySongArtist.artistId,
                    RelationEntityArtistCountry.countryId,
                    EntityArtist.popularity,
                    rank
                )
                .alias("RankedSongs")
        }

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
            println("Inserting ${it.size} artist popular songs")

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

                        listOf(
                            rem,
                            ins
                        ).awaitAll()
                    }
                }
        }
    }

    override suspend fun updateArtistMostPopularSongs() {
        coroutineScope {// TODO: idea create index based fetch of entry as size is big

        }
    }

    private suspend fun getCountrysMostPopularSongs(): List<DtoCountryPopularSong> {
        val songId = EntitySong.id.alias("songId")
        val countryId = EntityCountry.id.alias("countryId")

        val rank = kyokuDbQuery {
            RowNumber()
                .over()
                .partitionBy(EntityCountry.id)
                .orderBy(EntitySongInfo.popularity, SortOrder.DESC)
                .alias("rankk")
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
                .select(
                    songId,
                    EntitySongInfo.popularity,
                    countryId,
                    rank
                )
                .alias("RankedSongs")
        }

        return kyokuDbQuery {
            rankedQuery.selectAll().where {
                (rankedQuery[rank]).lessEq(longLiteral(COUNTRY_MOST_POPULAR_SONGS_LIMIT))
            }
                .orderBy(
                    rankedQuery[countryId] to SortOrder.DESC,
                    rankedQuery[EntitySongInfo.popularity] to SortOrder.DESC
                ).map {
                    DtoCountryPopularSong(
                        songId = it[rankedQuery.fields[0]].toString().toLong(),
                        countryId = it[rankedQuery.fields[2]].toString().toInt(),
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

    private suspend fun getShardRelationEntityGenreTypeArtist(
        genre: Genre,
    ) = shardGenreArtistDbQuery {
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