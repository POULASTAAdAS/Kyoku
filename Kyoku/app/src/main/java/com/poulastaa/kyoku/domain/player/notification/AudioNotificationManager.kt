package com.poulastaa.kyoku.domain.player.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.annotation.OptIn
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.ui.PlayerNotificationManager
import com.poulastaa.kyoku.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val NOTIFICATION_ID = 1
private const val NOTIFICATION_CHANNEL_NAME = "kyoku player"
private const val NOTIFICATION_CHANNEL_ID = "kyoku player id"

class AudioNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val player: ExoPlayer
) {
    private val notificationManager: NotificationManagerCompat =
        NotificationManagerCompat.from(context)

    init {
        createNotificationChannel()
    }

    fun startNotificationService(
        service: MediaSessionService,
        session: MediaSession
    ) {
        buildNotification(session)
        startForGroundNotificationService(service)
    }

    private fun createNotificationChannel() = notificationManager.createNotificationChannel(
        NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
    )

    @OptIn(UnstableApi::class)
    private fun buildNotification(session: MediaSession) = PlayerNotificationManager.Builder(
        context,
        NOTIFICATION_ID,
        NOTIFICATION_CHANNEL_ID
    ).setMediaDescriptionAdapter(
        AudioNotificationAdapter(
            context = context,
            pendingIntent = session.sessionActivity
        )
    ).setSmallIconResourceId(R.drawable.night_logo).build()
        .also {
            it.setMediaSessionToken(session.sessionCompatToken)
            it.setUseFastForwardActionInCompactView(true)
            it.setUseRewindActionInCompactView(true)
            it.setPriority(NotificationCompat.PRIORITY_HIGH)
            it.setPlayer(player)
        }

    private fun startForGroundNotificationService(service: MediaSessionService) {
        val notification = Notification.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        service.startForeground(NOTIFICATION_ID, notification)
    }
}