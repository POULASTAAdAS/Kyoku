package com.poulastaa.kyoku.shardmanager.app.data.repository

import com.poulastaa.kyoku.shardmanager.app.core.database.repository.ExposedLocalShardUpdateDatasource
import com.poulastaa.kyoku.shardmanager.app.core.domain.utils.CURRENT_TIME
import kotlinx.coroutines.runBlocking
import org.quartz.Job
import org.quartz.JobExecutionContext

class ShardUpdateJobArtistGenre : Job {
    private val db = ExposedLocalShardUpdateDatasource.instance()

    override fun execute(p0: JobExecutionContext?) = runBlocking {
        println("$CURRENT_TIME Updating genre most popular artists")
        db.updateShardGenreArtistsRelation()
        println("$CURRENT_TIME Finished updating genre most popular artists")
    }
}