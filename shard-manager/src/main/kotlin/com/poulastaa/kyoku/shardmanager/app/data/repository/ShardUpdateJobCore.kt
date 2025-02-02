package com.poulastaa.kyoku.shardmanager.app.data.repository

import com.poulastaa.kyoku.shardmanager.app.core.database.repository.ExposedLocalShardUpdateDatasource
import kotlinx.coroutines.*
import org.quartz.Job
import org.quartz.JobExecutionContext

class ShardUpdateJobCore : Job {
    private val db = ExposedLocalShardUpdateDatasource.instance()

    override fun execute(p0: JobExecutionContext?) {
        println("Updating core")

        CoroutineScope(Dispatchers.IO).launch {
            listOf(
                async { db.updateSongPopularity() },
                async { db.updateArtistPopularity() }
            ).awaitAll()
        }
    }
}