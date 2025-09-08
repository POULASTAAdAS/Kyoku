package com.poulastaa.kyoku.notification.config

import com.poulastaa.kyoku.notification.model.Notification
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitConfig {
    @Bean
    fun emailQueue() = Queue(Notification.QUEUE.EMAIL)

    @Bean
    fun provideJackson2Converter() = Jackson2JsonMessageConverter().apply {
        setClassMapper(
            DefaultJackson2JavaTypeMapper().apply {
                setTrustedPackages("com.poulastaa.*")
            }
        )
    }

    @Bean
    fun provideRabbitTemplate(
        cf: ConnectionFactory,
        converter: Jackson2JsonMessageConverter,
    ) = RabbitTemplate(cf).apply {
        messageConverter = converter
    }

    @Bean
    fun provideRabbitListenerContainerFactory(
        cf: ConnectionFactory,
        converter: Jackson2JsonMessageConverter,
    ) = SimpleRabbitListenerContainerFactory().apply {
        setConnectionFactory(cf)
        setMessageConverter(converter)
    }
}