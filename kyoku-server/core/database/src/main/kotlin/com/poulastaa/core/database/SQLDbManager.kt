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
    private var IS_INITIALIZED = false

    @Synchronized
    fun initializeDatabases(
        driverClass: String,
        userDbUrl: String,
        kyokuDbUrl: String,
    ) {
        require(userDbUrl.isNotBlank()) { "USER JDBC URL cannot be blank" }
        require(kyokuDbUrl.isNotBlank()) { "KYOKU JDBC URL cannot be blank" }
        require(driverClass.isNotBlank()) { "Driver class cannot be blank" }

        if (IS_INITIALIZED) throw IllegalStateException("Databases are already initialized!")

        USER_DB = Database.Companion.connect(
            provideDatasource(
                jdbcUrl = userDbUrl,
                driverClass = driverClass,
            )
        )

        KYOKU_DB = Database.Companion.connect(
            provideDatasource(
                jdbcUrl = kyokuDbUrl,
                driverClass = driverClass,
                maximumPoolSize = 20
            )
        )

        transaction(USER_DB) {
            addLogger(StdOutSqlLogger)
        }
        transaction(KYOKU_DB) {
            addLogger(StdOutSqlLogger)
        }

        IS_INITIALIZED = true
    }

    internal suspend fun <T> userDbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(context = Dispatchers.IO, db = USER_DB) {
            block()
        }

    internal suspend fun <T> kyokuDbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(context = Dispatchers.IO, db = KYOKU_DB) {
            block()
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