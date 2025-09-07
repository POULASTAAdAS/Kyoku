package com.poulastaa.kyoku.notification.model

import com.poulastaa.kyoku.notification.utils.Email
import com.poulastaa.kyoku.notification.utils.Username

data class NotificationPayload<T>(
    val username: Username,
    val email: Email,
    val data: T,
    val endPoint: String? = null,
    val type: Notification.Type,
)
