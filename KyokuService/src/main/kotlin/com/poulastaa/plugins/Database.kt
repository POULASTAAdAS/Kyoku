package com.poulastaa.plugins

import com.poulastaa.domain.table.PinnedTable
import com.poulastaa.domain.table.relation.*
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import io.ktor.server.application.hooks.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.getScopeId
import org.koin.core.component.getScopeName

fun Application.configureDatabase() {
    val driverClass = environment.config.property("storage.driverClassName").getString()
    val jdbcUrl = environment.config.property("storage.jdbcURL").getString()

    val db = Database.connect(
        provideDataSource(
            url = jdbcUrl,
            driverClass = driverClass
        )
    )

    transaction(db) {
        SchemaUtils.create(UserPlaylistSongRelationTable)
        SchemaUtils.create(UserPlaylistRelationTable)
        SchemaUtils.create(UserGenreRelationTable)
        SchemaUtils.create(UserArtistRelationTable)
        SchemaUtils.create(UserAlbumRelationTable)
        SchemaUtils.create(UserFavouriteRelationTable)
        SchemaUtils.create(PinnedTable)
    }
}

private fun provideDataSource(
    url: String,
    driverClass: String,
): HikariDataSource = HikariDataSource(
    HikariConfig().apply {
        driverClassName = driverClass
        jdbcUrl = url
        maximumPoolSize = 50
        connectionTimeout = 60_000
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        validate()
    }
)

suspend fun <T> query(block: suspend () -> T): T {
    return try {
        newSuspendedTransaction(
            Dispatchers.IO,
            statement = { block() }
        )
    } catch (e: Exception) {
        throw e
    }
}