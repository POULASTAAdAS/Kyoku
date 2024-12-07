package com.poulastaa.app.plugins

import com.poulastaa.core.database.user.DbManager
import io.ktor.server.application.*

fun Application.configureDatabases() {
    val driverClass = environment.config.property("storage.driverClassName").getString()

    val userJdbcUrl = environment.config.property("storage.userJdbcURL").getString()
    val kyokuJdbcUrl = environment.config.property("storage.JdbcURL").getString()

    DbManager.initializeDatabases(
        driverClass = driverClass,
        userDbUrl = userJdbcUrl,
        kyokuDbUrl = kyokuJdbcUrl
    )
}