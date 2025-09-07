package com.poulastaa.kyoku.notification.service

import com.poulastaa.kyoku.notification.model.Notification
import com.poulastaa.kyoku.notification.model.NotificationPayload
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class NotificationService(
    private val mail: JavaMailService,
) {
    @RabbitListener(
        queues = [Notification.QUEUE.EMAIL],
        containerFactory = "provideRabbitListenerContainerFactory"
    )
    fun consumeMail(message: NotificationPayload<Any>) {
        when (message.type) {
            Notification.Type.WELCOME -> mail.sendWelcomeMail(message.email, message.username)
            Notification.Type.WELCOME_BACK -> mail.sendWelcomeBackMail(message.email, message.username)
            Notification.Type.AUTHENTICATE -> message.endPoint?.let { endPoint ->
                mail.sendVerificationMail(
                    message.email,
                    message.username,
                    endPoint,
                    "token" // TODO add token
                )
            }
        }
    }
}