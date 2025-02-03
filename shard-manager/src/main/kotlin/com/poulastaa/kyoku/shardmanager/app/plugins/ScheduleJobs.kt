package com.poulastaa.kyoku.shardmanager.app.plugins

import com.poulastaa.kyoku.shardmanager.app.data.repository.ShardUpdateJobArtistGenre
import com.poulastaa.kyoku.shardmanager.app.data.repository.ShardUpdateJobCore
import com.poulastaa.kyoku.shardmanager.app.data.repository.ShardUpdateJobSuggestion
import org.quartz.CronScheduleBuilder
import org.quartz.JobBuilder
import org.quartz.TriggerBuilder
import org.quartz.impl.StdSchedulerFactory


private var IS_INITIALIZED = false

// default time set to 12 am
private const val TRIGGER_HOUR = 0
private const val TRIGGER_MINUTE = 0

@Synchronized
fun scheduleJobs() {
    if (IS_INITIALIZED) throw IllegalStateException("Jobs already initialized")

    val scheduler = StdSchedulerFactory.getDefaultScheduler().apply { start() }

    val jobUpdateCore = JobBuilder.newJob(ShardUpdateJobCore::class.java)
        .withIdentity("updateCore", "upsert")
        .build()
    val jobUpdateGenresMostPopularArtist = JobBuilder.newJob(ShardUpdateJobArtistGenre::class.java)
        .withIdentity("updateGenresMostPopularArtist", "upsert")
        .build()
    val jobUpdateSuggestion = JobBuilder.newJob(ShardUpdateJobSuggestion::class.java)
        .withIdentity("updateSuggestion", "upsert")
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