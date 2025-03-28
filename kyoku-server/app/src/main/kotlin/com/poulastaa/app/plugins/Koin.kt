package com.poulastaa.app.plugins

import com.poulastaa.auth.data.di.provideAuthService
import com.poulastaa.auth.data.di.provideJWTService
import com.poulastaa.core.data.di.provideGsonService
import com.poulastaa.core.data.di.provideSessionService
import com.poulastaa.core.database.di.provideCoreDatabaseService
import com.poulastaa.core.database.di.provideJedisPoolService
import com.poulastaa.notification.data.di.provideNotificationService
import com.poulastaa.search.data.di.provideArtistPagerDataService
import com.poulastaa.suggestion.data.di.provideSuggestionDataService
import com.poulastaa.sync.di.provideSyncDataService
import com.poulastaa.user.data.di.provideUserDataRepositoryService
import com.poulastaa.view.data.di.provideViewDataService
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.configureKoin(
    issuer: String,
    audience: String,
    privateKeyPayload: String,
    redisHost: String,
    redisPort: Int,
    redisPassword: String,
) {
    install(Koin) {
        modules(
            provideGsonService(),
            provideSessionService(),
            provideJedisPoolService(
                redisHost = redisHost,
                redisPort = redisPort,
                redisPassword = redisPassword
            ),
            provideCoreDatabaseService(),
            provideJWTService(
                issuer = issuer,
                audience = audience,
                privateKeyPayload = privateKeyPayload
            ),
            provideAuthService(),
            provideNotificationService(),
            provideUserDataRepositoryService(),
            provideSuggestionDataService(),
            provideViewDataService(),
            provideSyncDataService(),
            provideArtistPagerDataService()
        )
    }
}