package com.poulastaa.kyoku.shardmanager.app.data.repository

import com.poulastaa.kyoku.shardmanager.app.core.database.repository.ExposedLocalShardUpdateDatasource
import kotlinx.coroutines.*
import org.quartz.Job
import org.quartz.JobExecutionContext

class ShardUpdateJobSuggestion : Job {
    private val db = ExposedLocalShardUpdateDatasource.instance()

    override fun execute(p0: JobExecutionContext?) {
        println("Updating Suggestions")

        CoroutineScope(Dispatchers.IO).launch {
            listOf(
                async { db.updateArtistMostPopularSongs() },
                async { db.updateCountryMostPopularSongs() },
                async { db.updateYearMostPopularSongs() }
            ).awaitAll()
        }
    }
}