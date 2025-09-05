package com.poulastaa.kyoku.auth.config

import com.poulastaa.kyoku.auth.model.Notification
import org.springframework.amqp.core.Queue

class RabbitQueues {
    fun init() = Queue(Notification.Channel.EMAIL.name)
}