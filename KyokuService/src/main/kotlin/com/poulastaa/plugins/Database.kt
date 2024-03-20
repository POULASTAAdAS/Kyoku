package com.poulastaa.plugins

import com.poulastaa.data.model.db_table.PlaylistTable
import com.poulastaa.data.model.db_table.user.EmailAuthUserTable
import com.poulastaa.data.model.db_table.user.GoogleAuthUserTable
import com.poulastaa.data.model.db_table.user.PasskeyAuthUserTable
import com.poulastaa.data.model.db_table.user_album.EmailUserAlbumRelation
import com.poulastaa.data.model.db_table.user_album.GoogleUserAlbumRelation
import com.poulastaa.data.model.db_table.user_album.PasskeyUserAlbumRelation
import com.poulastaa.data.model.db_table.user_artist.EmailUserArtistRelationTable
import com.poulastaa.data.model.db_table.user_artist.GoogleUserArtistRelationTable
import com.poulastaa.data.model.db_table.user_artist.PasskeyUserArtistRelationTable
import com.poulastaa.data.model.db_table.user_fev.EmailUserFavouriteTable
import com.poulastaa.data.model.db_table.user_fev.GoogleUserFavouriteTable
import com.poulastaa.data.model.db_table.user_fev.PasskeyUserFavouriteTable
import com.poulastaa.data.model.db_table.user_genre.EmailUserGenreRelationTable
import com.poulastaa.data.model.db_table.user_genre.GoogleUserGenreRelationTable
import com.poulastaa.data.model.db_table.user_genre.PasskeyUserGenreRelationTable
import com.poulastaa.data.model.db_table.user_listen_history.EmailUserListenHistoryTable
import com.poulastaa.data.model.db_table.user_listen_history.GoogleUserListenHistoryTable
import com.poulastaa.data.model.db_table.user_listen_history.PasskeyUserListenHistoryTable
import com.poulastaa.data.model.db_table.user_playlist.EmailUserPlaylistTable
import com.poulastaa.data.model.db_table.user_playlist.GoogleUserPlaylistTable
import com.poulastaa.data.model.db_table.user_playlist.PasskeyUserPlaylistTable
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
        SchemaUtils.create(PlaylistTable)

        SchemaUtils.create(EmailUserAlbumRelation)
        SchemaUtils.create(GoogleUserAlbumRelation)
        SchemaUtils.create(PasskeyUserAlbumRelation)

        SchemaUtils.create(EmailUserArtistRelationTable)
        SchemaUtils.create(GoogleUserArtistRelationTable)
        SchemaUtils.create(PasskeyUserArtistRelationTable)

        SchemaUtils.create(EmailUserGenreRelationTable)
        SchemaUtils.create(GoogleUserGenreRelationTable)
        SchemaUtils.create(PasskeyUserGenreRelationTable)

        SchemaUtils.create(EmailUserListenHistoryTable)
        SchemaUtils.create(GoogleUserListenHistoryTable)
        SchemaUtils.create(PasskeyUserListenHistoryTable)

        SchemaUtils.create(EmailUserPlaylistTable)
        SchemaUtils.create(GoogleUserPlaylistTable)
        SchemaUtils.create(PasskeyUserPlaylistTable)

        SchemaUtils.create(EmailUserFavouriteTable)
        SchemaUtils.create(GoogleUserFavouriteTable)
        SchemaUtils.create(PasskeyUserFavouriteTable)
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