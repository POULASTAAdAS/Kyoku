package com.poulastaa.app.plugins

import com.poulastaa.core.database.user.userDatabase
import io.ktor.server.application.*

fun Application.configureDatabases() {
    val driverClass = environment.config.property("storage.driverClassName").getString()
    val userJdbcUrl = environment.config.property("storage.userJdbcURL").getString()

    userDatabase(
        driverClass = driverClass,
        jdbcUrl = userJdbcUrl
    )
}