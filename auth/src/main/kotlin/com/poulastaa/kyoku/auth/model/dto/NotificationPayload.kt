package com.poulastaa.kyoku.auth.model.dto

import com.poulastaa.kyoku.auth.model.Notification
import com.poulastaa.kyoku.auth.utils.Email
import com.poulastaa.kyoku.auth.utils.Username

data class NotificationPayload<T>(
    val username: Username,
    val email: Email,
    val data: T,
    val endPoint: String? = null,
    val type: Notification.Type,
)
