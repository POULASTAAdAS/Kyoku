package com.poulastaa.kyoku.auth.service

import com.poulastaa.kyoku.auth.model.Notification
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class NotificationService(
    private val queue: RabbitTemplate,
) {
    fun publishMail(notification: Notification) {
        when (notification) {
            is Notification.Email -> {
                queue.convertAndSend(notification.channel, notification.data) {
                    it.apply {
                        messageProperties.expiration = notification.type.prop.expTime.inWholeMilliseconds.toString()
                    }
                }
            }
        }
    }
}