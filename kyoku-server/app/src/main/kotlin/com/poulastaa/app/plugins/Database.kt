package com.poulastaa.app.plugins

import com.poulastaa.core.database.SQLDbManager
import io.ktor.server.application.*

fun Application.configureDatabases() {
    val driverClass = environment.config.property("storage.driverClassName").getString()

    val userJdbcUrl = environment.config.property("storage.userJdbcURL").getString()
    val kyokuJdbcUrl = environment.config.property("storage.kyokuJdbcURL").getString()
    val genreArtistShardDbUrl = environment.config.property("storage.genreArtistShard").getString()
    val popularShardDbUrl = environment.config.property("storage.popularShard").getString()
    val pagingSearchDbUrl = environment.config.property("storage.pagingShard").getString()

    SQLDbManager.initializeDatabases(
        driverClass = driverClass,
        userDbUrl = userJdbcUrl,
        kyokuDbUrl = kyokuJdbcUrl,
        genreArtistShardDbUrl = genreArtistShardDbUrl,
        popularShardDbUrl = popularShardDbUrl,
        pagingSearchDbUrl = pagingSearchDbUrl
    )
}