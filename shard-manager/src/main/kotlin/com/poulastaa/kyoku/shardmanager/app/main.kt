package com.poulastaa.kyoku.shardmanager.app

import com.poulastaa.kyoku.shardmanager.app.core.database.repository.ExposedLocalShardUpdateDatasource
import com.poulastaa.kyoku.shardmanager.app.plugins.configureDatabase
import com.poulastaa.kyoku.shardmanager.app.plugins.scheduleJobs

fun main() {
    val db = ExposedLocalShardUpdateDatasource.instance()

    configureDatabase(db)
    scheduleJobs()
}