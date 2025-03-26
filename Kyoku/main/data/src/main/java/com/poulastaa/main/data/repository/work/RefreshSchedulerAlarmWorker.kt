package com.poulastaa.main.data.repository.work

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.poulastaa.main.domain.repository.work.RefreshScheduler
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlin.time.toJavaDuration

internal class RefreshSchedulerAlarmWorker @Inject constructor(
    @ApplicationContext private val context: Context,
    private val scope: CoroutineScope,
) : RefreshScheduler {
    private val workManager = WorkManager.getInstance(context)
    private val alarm = context.getSystemService(AlarmManager::class.java)

    private enum class ALARM_TYPE(val value: Int) {
        MIDNIGHT_REFRESH(1)
    }

    override fun scheduleRefresh() {
        val triggerTime = calculateNextMidnightMillis()
        val pendingIntent = createAlarmIntent()

        if (hasExactAlarmPermission()) alarm.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            pendingIntent
        ) else handleMissingPermission()
    }

    override fun cancelRefresh() = alarm.cancel(createAlarmIntent())

    private fun calculateNextMidnightMillis(): Long {
        return Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            add(Calendar.DAY_OF_YEAR, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    private fun createAlarmIntent(): PendingIntent {
        val intent = Intent(context, RefreshReceiver::class.java)
        return PendingIntent.getBroadcast(
            context,
            ALARM_TYPE.MIDNIGHT_REFRESH.value,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun hasExactAlarmPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) alarm.canScheduleExactAlarms()
        else true
    }

    private fun handleMissingPermission() {
        scope.launch {
            val isSyncScheduled = withContext(Dispatchers.IO) {
                workManager.getWorkInfosByTag(ALARM_TYPE.MIDNIGHT_REFRESH.name)
                    .get().any {
                        it.state == WorkInfo.State.ENQUEUED ||
                                it.state == WorkInfo.State.RUNNING ||
                                it.state == WorkInfo.State.SUCCEEDED
                    }
            }

            if (isSyncScheduled) return@launch

            val req = PeriodicWorkRequestBuilder<MidNightRefreshWorker>(
                repeatInterval = 1.0.toDuration(DurationUnit.DAYS).toJavaDuration()
            ).setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            ).setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 15,
                timeUnit = TimeUnit.MINUTES
            ).setInitialDelay(
                duration = 15,
                timeUnit = TimeUnit.HOURS
            ).addTag(ALARM_TYPE.MIDNIGHT_REFRESH.name).build()

            workManager.enqueueUniquePeriodicWork(
                ALARM_TYPE.MIDNIGHT_REFRESH.name,
                ExistingPeriodicWorkPolicy.REPLACE,
                req
            )
        }
    }
}