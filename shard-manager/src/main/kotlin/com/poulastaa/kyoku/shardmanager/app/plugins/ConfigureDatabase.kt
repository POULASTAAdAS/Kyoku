package com.poulastaa.kyoku.shardmanager.app.plugins

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.poulastaa.kyoku.shardmanager.app.core.database.model.DatabasePayload
import com.poulastaa.kyoku.shardmanager.app.core.database.utils.populateGenreArtistShardTables
import com.poulastaa.kyoku.shardmanager.app.core.database.utils.populatePagingShardTables
import com.poulastaa.kyoku.shardmanager.app.core.database.utils.populateSuggestionShardTables
import com.poulastaa.kyoku.shardmanager.app.core.domain.repository.LocalShardUpdateDatasource
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.io.File

private var IS_INITIALIZED = false

private lateinit var KYOKU_DB: Database
private lateinit var GENRE_ARTIST_SHARD_DB: Database
private lateinit var POPULAR_SONG_SHARD_DB: Database
private lateinit var PAGING_SHARD_DB: Database

suspend fun <T> kyokuDbQuery(block: suspend () -> T): T =
    newSuspendedTransaction(context = Dispatchers.IO, db = KYOKU_DB) {
        block()
    }

suspend fun <T> shardGenreArtistDbQuery(block: suspend () -> T): T =
    newSuspendedTransaction(context = Dispatchers.IO, db = GENRE_ARTIST_SHARD_DB) {
        block()
    }

suspend fun <T> shardPopularDbQuery(block: suspend () -> T): T =
    newSuspendedTransaction(context = Dispatchers.IO, db = POPULAR_SONG_SHARD_DB) {
        block()
    }


suspend fun <T> shardSearchDbQuery(block: suspend () -> T): T =
    newSuspendedTransaction(context = Dispatchers.IO, db = PAGING_SHARD_DB) {
        block()
    }

@Synchronized
fun configureDatabase(db: LocalShardUpdateDatasource) {
    if (IS_INITIALIZED) throw IllegalArgumentException("Database already initialized")
    IS_INITIALIZED = true

    val payload = getDatabasePayload()

    KYOKU_DB = Database.Companion.connect(
        provideDatasource(
            jdbcUrl = payload.kyokuUrl,
            driverClass = payload.driverClassName,
        )
    )

    GENRE_ARTIST_SHARD_DB = Database.Companion.connect(
        provideDatasource(
            jdbcUrl = payload.shardGenreArtistUrl,
            driverClass = payload.driverClassName,
        )
    )

    POPULAR_SONG_SHARD_DB = Database.Companion.connect(
        provideDatasource(
            jdbcUrl = payload.shardPopularSongUrl,
            driverClass = payload.driverClassName,
        )
    )

    PAGING_SHARD_DB = Database.Companion.connect(
        provideDatasource(
            jdbcUrl = payload.pagingSearchUrl,
            driverClass = payload.driverClassName,
            maximumPoolSize = 30
        )
    )

    populateGenreArtistShardTables(db)
    populateSuggestionShardTables(db)
    populatePagingShardTables(db)
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
        validate()
    }
)

private fun getDatabasePayload(): DatabasePayload {
    val gson = Gson()

    val str = File("src/main/resources/res.json").readText()
    val obj = gson.fromJson(str, JsonObject::class.java)

    val storage = obj.get("storage").asJsonObject

    return DatabasePayload(
        driverClassName = storage.get("driverClassName").asString,
        kyokuUserUrl = storage.get("userJdbcURL").asString,
        kyokuUrl = storage.get("kyokuJdbcURL").asString,
        shardGenreArtistUrl = storage.get("genreArtistShard").asString,
        shardPopularSongUrl = storage.get("shardPopularSongUrl").asString,
        pagingSearchUrl = storage.get("pagingSearchUrl").asString,
    )
}