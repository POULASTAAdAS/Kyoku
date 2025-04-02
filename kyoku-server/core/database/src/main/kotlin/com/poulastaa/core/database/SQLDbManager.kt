package com.poulastaa.core.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object SQLDbManager {
    private lateinit var USER_DB: Database
    private lateinit var KYOKU_DB: Database
    private lateinit var GENRE_ARTIST_SHARD_DB: Database
    private lateinit var POPULAR_SONG_SHARD_DB: Database
    private lateinit var PAGING_SHARD_DB: Database

    private var IS_INITIALIZED = false

    internal suspend fun <T> userDbQuery(
        block: suspend () -> T,
    ) = newSuspendedTransaction(context = Dispatchers.IO, db = USER_DB) {
        block()
    }

    internal suspend fun <T> kyokuDbQuery(
        block: suspend () -> T,
    ) = newSuspendedTransaction(context = Dispatchers.IO, db = KYOKU_DB) {
        block()
    }

    internal suspend fun <T> shardGenreArtistDbQuery(
        block: suspend () -> T,
    ) = newSuspendedTransaction(context = Dispatchers.IO, db = GENRE_ARTIST_SHARD_DB) {
        block()
    }

    suspend fun <T> shardPopularDbQuery(
        block: suspend () -> T,
    ) = newSuspendedTransaction(context = Dispatchers.IO, db = POPULAR_SONG_SHARD_DB) {
        block()
    }

    suspend fun <T> shardSearchDbQuery(
        block: suspend () -> T,
    ): T = newSuspendedTransaction(context = Dispatchers.IO, db = PAGING_SHARD_DB) {
        block()
    }

    @Synchronized
    fun initializeDatabases(
        driverClass: String,
        userDbUrl: String,
        kyokuDbUrl: String,
        genreArtistShardDbUrl: String,
        popularShardDbUrl: String,
        pagingSearchDbUrl: String,
    ) {
        require(driverClass.isNotBlank()) { "Driver class cannot be blank" }

        require(userDbUrl.isNotBlank()) { "USER JDBC URL cannot be blank" }
        require(kyokuDbUrl.isNotBlank()) { "KYOKU JDBC URL cannot be blank" }
        require(genreArtistShardDbUrl.isNotBlank()) { "ARTIST SHARD JDBC URL cannot be blank" }
        require(popularShardDbUrl.isNotBlank()) { "POPULAR SHARD JDBC URL cannot be blank" }
        require(pagingSearchDbUrl.isNotBlank()) { "PAGING SHARD JDBC URL cannot be blank" }

        if (IS_INITIALIZED) throw IllegalStateException("Databases are already initialized!")

        USER_DB = Database.Companion.connect(
            provideDatasource(
                jdbcUrl = userDbUrl,
                driverClass = driverClass,
                maximumPoolSize = 15
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

        POPULAR_SONG_SHARD_DB = Database.Companion.connect(
            provideDatasource(
                jdbcUrl = popularShardDbUrl,
                driverClass = driverClass,
                maximumPoolSize = 10
            )
        )

        PAGING_SHARD_DB = Database.Companion.connect(
            provideDatasource(
                jdbcUrl = pagingSearchDbUrl,
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
            addLogger(StdOutSqlLogger)
        }
        transaction(POPULAR_SONG_SHARD_DB) {
            addLogger(StdOutSqlLogger)
        }
        transaction(PAGING_SHARD_DB) {
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
}