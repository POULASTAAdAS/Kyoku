package com.poulastaa.core.database.user

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

fun userDatabase(
    driverClass: String,
    jdbcUrl: String,
) {
    val db = Database.connect(
        provideDatasource(
            jdbcUrl = jdbcUrl,
            driverClass = driverClass,
        )
    )

    transaction(db) {
        addLogger(StdOutSqlLogger)
    }
}

private fun provideDatasource(
    driverClass: String,
    jdbcUrl: String,
) = HikariDataSource(
    HikariConfig().apply {
        driverClassName = driverClass
        this.jdbcUrl = jdbcUrl
        maximumPoolSize = 20
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        validate()
    }
)