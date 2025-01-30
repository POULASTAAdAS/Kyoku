package com.poulastaa.core.database

import com.poulastaa.core.database.dao.DaoArtist
import com.poulastaa.core.database.dao.ShardDaoArtist
import com.poulastaa.core.database.entity.app.*
import com.poulastaa.core.database.entity.model.DtoGenreArtistRelation
import com.poulastaa.core.database.mapper.toDbArtistDto
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object SQLDbManager {
    private lateinit var USER_DB: Database
    private lateinit var KYOKU_DB: Database
    private lateinit var GENRE_ARTIST_SHARD_DB: Database
    private var IS_INITIALIZED = false

    internal suspend fun <T> userDbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(context = Dispatchers.IO, db = USER_DB) {
            block()
        }

    internal suspend fun <T> kyokuDbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(context = Dispatchers.IO, db = KYOKU_DB) {
            block()
        }

    internal suspend fun <T> shardGenreArtistDbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(context = Dispatchers.IO, db = GENRE_ARTIST_SHARD_DB) {
            block()
        }

    @Synchronized
    fun initializeDatabases(
        driverClass: String,
        userDbUrl: String,
        kyokuDbUrl: String,
        genreArtistShardDbUrl: String,
    ) {
        require(userDbUrl.isNotBlank()) { "USER JDBC URL cannot be blank" }
        require(kyokuDbUrl.isNotBlank()) { "KYOKU JDBC URL cannot be blank" }
        require(genreArtistShardDbUrl.isNotBlank()) { "ARTIST SHARD JDBC URL cannot be blank" }
        require(driverClass.isNotBlank()) { "Driver class cannot be blank" }

        if (IS_INITIALIZED) throw IllegalStateException("Databases are already initialized!")

        USER_DB = Database.Companion.connect(
            provideDatasource(
                jdbcUrl = userDbUrl,
                driverClass = driverClass
            )
        )

        KYOKU_DB = Database.Companion.connect(
            provideDatasource(
                jdbcUrl = kyokuDbUrl,
                driverClass = driverClass,
                maximumPoolSize = 20
            )
        )

        GENRE_ARTIST_SHARD_DB = Database.Companion.connect(
            provideDatasource(
                jdbcUrl = genreArtistShardDbUrl,
                driverClass = driverClass,
                maximumPoolSize = 10
            )
        )

        transaction(USER_DB) {
            addLogger(StdOutSqlLogger)
        }
        transaction(KYOKU_DB) {
            addLogger(StdOutSqlLogger)
        }
        transaction(GENRE_ARTIST_SHARD_DB) {
//            createGenreArtistShardTables() // TODO temp comment as takes to much time to start
            addLogger(StdOutSqlLogger)
        }

        IS_INITIALIZED = true
    }

    private fun provideDatasource(
        driverClass: String,
        jdbcUrl: String,
        maximumPoolSize: Int = 15,
    ) = HikariDataSource(
        HikariConfig().apply {
            driverClassName = driverClass
            this.jdbcUrl = jdbcUrl
            this.maximumPoolSize = maximumPoolSize
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
    )

    private fun createGenreArtistShardTables() = runBlocking {
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
                val artist = kyokuDbQuery { DaoArtist.all().map { it.toDbArtistDto() } }

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
}