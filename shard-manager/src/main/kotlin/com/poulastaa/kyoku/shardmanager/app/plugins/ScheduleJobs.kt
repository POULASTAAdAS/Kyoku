package com.poulastaa.kyoku.shardmanager.app.plugins

import com.poulastaa.kyoku.shardmanager.app.data.repository.ShardUpdateJobArtistGenre
import com.poulastaa.kyoku.shardmanager.app.data.repository.ShardUpdateJobCore
import com.poulastaa.kyoku.shardmanager.app.data.repository.ShardUpdateJobSuggestion
import org.quartz.CronScheduleBuilder
import org.quartz.JobBuilder
import org.quartz.TriggerBuilder
import org.quartz.impl.StdSchedulerFactory


private var IS_INITIALIZED = false

private const val TRIGGER_HOUR = 20
private const val TRIGGER_MINUTE = 23

@Synchronized
fun scheduleJobs() {
    if (IS_INITIALIZED) throw IllegalStateException("Jobs already initialized")

    val scheduler = StdSchedulerFactory.getDefaultScheduler().apply { start() }

    val jobUpdateCore = JobBuilder.newJob(ShardUpdateJobCore::class.java)
        .withIdentity("updateCore", "update")
        .build()
    val jobUpdateGenresMostPopularArtist = JobBuilder.newJob(ShardUpdateJobArtistGenre::class.java)
        .withIdentity("updateGenresMostPopularArtist", "update")
        .build()
    val jobUpdateSuggestion = JobBuilder.newJob(ShardUpdateJobSuggestion::class.java)
        .withIdentity("updateSuggestion", "update")
        .build()

    val trigger = TriggerBuilder.newTrigger()
        .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(TRIGGER_HOUR, TRIGGER_MINUTE))

    scheduler.scheduleJob(
        jobUpdateCore,
        trigger.withIdentity("updateCore").build()
    )
    scheduler.scheduleJob(
        jobUpdateGenresMostPopularArtist,
        trigger.withIdentity("updateGenresMostPopularArtist").build()
    )
    scheduler.scheduleJob(
        jobUpdateSuggestion,
        trigger.withIdentity("updateSuggestion").build()
    )
}