package com.poulastaa.kyoku.shardmanager.app.data.repository

import com.poulastaa.kyoku.shardmanager.app.core.database.repository.ExposedLocalShardUpdateDatasource
import com.poulastaa.kyoku.shardmanager.app.core.domain.utils.CURRENT_TIME
import kotlinx.coroutines.*
import org.quartz.Job
import org.quartz.JobExecutionContext

class ShardUpdateJobCore : Job {
    private val db = ExposedLocalShardUpdateDatasource.instance()

    override fun execute(p0: JobExecutionContext?) {
        CoroutineScope(Dispatchers.IO).launch {
            println("$CURRENT_TIME Updating core")

            listOf(
                async { db.upsertSongPopularity() },
                async { db.upsertArtistPopularity() }
            ).awaitAll()

            println("$CURRENT_TIME Finished updating core")
        }
    }
}