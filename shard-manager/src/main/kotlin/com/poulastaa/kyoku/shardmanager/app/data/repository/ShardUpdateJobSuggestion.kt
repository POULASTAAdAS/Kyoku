package com.poulastaa.kyoku.shardmanager.app.data.repository

import com.poulastaa.kyoku.shardmanager.app.core.database.repository.ExposedLocalShardUpdateDatasource
import com.poulastaa.kyoku.shardmanager.app.core.domain.utils.CURRENT_TIME
import kotlinx.coroutines.*
import org.quartz.Job
import org.quartz.JobExecutionContext

class ShardUpdateJobSuggestion : Job {
    private val db = ExposedLocalShardUpdateDatasource.instance()

    override fun execute(p0: JobExecutionContext?) {

        CoroutineScope(Dispatchers.IO).launch {
            println("$CURRENT_TIME Updating suggestions")

            listOf(
                async { db.updateArtistMostPopularSongs() },
                async { db.updateCountryMostPopularSongs() },
                async { db.updateYearMostPopularSongs() }
            ).awaitAll()

            println("$CURRENT_TIME Finished updating suggestions")
        }
    }
}