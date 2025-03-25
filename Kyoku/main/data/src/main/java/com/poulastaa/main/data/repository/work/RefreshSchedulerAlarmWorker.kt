package com.poulastaa.main.data.repository.work

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import com.poulastaa.main.domain.repository.work.RefreshScheduler
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import javax.inject.Inject

internal class RefreshSchedulerAlarmWorker @Inject constructor(
    @ApplicationContext private val context: Context,
) : RefreshScheduler {
    private val alarm = context.getSystemService(AlarmManager::class.java)

    private enum class ALARM_TYPE(val value: Int) {
        MIDNIGHT_REFRESH(1)
    }

    override fun scheduleRefresh() {
        val triggerTime = calculateNextMidnightMillis()
        val pendingIntent = createAlarmIntent()

        alarm.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            pendingIntent
        )
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            context.startActivity(Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
        }
        // Consider fallback to WorkManager's periodic worker
    }
}