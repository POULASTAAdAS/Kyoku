package com.poulastaa.plugins

import com.poulastaa.data.model.db_table.playlist.EmailUserPlaylistTable
import com.poulastaa.data.model.db_table.playlist.GoogleUserPlaylistTable
import com.poulastaa.data.model.db_table.playlist.PasskeyUserPlaylistTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

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
        SchemaUtils.create(EmailUserPlaylistTable)
        SchemaUtils.create(GoogleUserPlaylistTable)
        SchemaUtils.create(PasskeyUserPlaylistTable)
    }
}

private fun provideDataSource(url: String, driverClass: String): HikariDataSource =
    HikariDataSource(
        HikariConfig().apply {
            driverClassName = driverClass
            jdbcUrl = url
            maximumPoolSize = 4
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
    )

suspend fun <T> dbQuery(block: suspend () -> T): T {
    return try {
        newSuspendedTransaction(
            Dispatchers.IO,
            statement = { block() }
        )
    } catch (e: Exception) {
        throw e
    }
}