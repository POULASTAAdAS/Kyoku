package com.poulastaa.notification.data.di

import com.poulastaa.notification.data.RedisMailSubscriber
import com.poulastaa.notification.data.repository.GoogleMailService
import com.poulastaa.notification.domain.repository.MailServiceRepository
import org.koin.dsl.module

fun provideNotificationService() = module {
    single<MailServiceRepository> {
        GoogleMailService(
            jwt = get()
        )
    }

    single<RedisMailSubscriber> {
        RedisMailSubscriber(
            cache = get(),
            mail = get(),
        )
    }
}