package com.poulastaa.kyoku.auth.service

import com.poulastaa.kyoku.auth.model.Notification
import com.poulastaa.kyoku.auth.model.dto.NotificationPayload
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class NotificationService(
    private val queue: RabbitTemplate,
) {
    fun publishMail(notification: Notification) {
        when (notification) {
            is Notification.Email -> {
                queue.convertAndSend(
                    notification.channel,
                    when (notification.type.prop) {
                        is Notification.Property.Endpoint -> notification.toNotificationPayload(notification.type.prop.endPoint)
                        is Notification.Property.Default -> notification.toNotificationPayload()
                    }
                ) {
                    it.apply {
                        messageProperties.expiration = notification.type.prop.expTime.inWholeMilliseconds.toString()
                    }
                }
            }
        }
    }

    private fun Notification.Email.toNotificationPayload(endpoint: String) = NotificationPayload(
        username = this.username,
        email = this.email,
        data = this.data,
        endPoint = endpoint,
        type = this.type,
    )

    private fun Notification.Email.toNotificationPayload() = NotificationPayload(
        username = this.username,
        email = this.email,
        data = this.data,
        type = this.type,
    )
}