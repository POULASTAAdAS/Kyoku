package com.poulastaa.kyoku.notification.service

import com.poulastaa.kyoku.notification.model.Notification
import com.poulastaa.kyoku.notification.model.NotificationPayload
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class NotificationService(
    private val mail: JavaMailService,
    private val jwt: JWTService,
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
                    email = message.email,
                    username = message.username,
                    endPoint = endPoint,
                    token = jwt.generateVerificationMailToken(message.email)
                )
            }

            Notification.Type.FORGOT_PASSWORD_CODE -> mail.sendForgotPasswordCodeWithMail(
                username = message.username,
                email = message.email,
                code = message.data.toString()
            )
        }
    }
}