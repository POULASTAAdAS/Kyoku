package com.poulastaa.app

import com.poulastaa.app.plugins.configureDatabases
import com.poulastaa.app.plugins.configureKoin
import com.poulastaa.app.plugins.configureRouting
import com.poulastaa.app.plugins.configureSerialization
import com.poulastaa.notification.data.RedisMailSubscriber
import io.ktor.server.application.*
import io.ktor.server.netty.*
import org.koin.ktor.ext.get

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureKoin(
        issuer = this.environment.config.property("jwt.issuer").getString(),
        audience = this.environment.config.property("jwt.audience").getString(),
        privateKeyPayload = this.environment.config.property("jwt.privateKey").getString(),
        redisHost = this.environment.config.property("cache.host").getString(),
        redisPort = this.environment.config.property("cache.port").getString().toInt(),
        redisPassword = this.environment.config.property("cache.password").getString(),
    )
    configureDatabases()
    configureRouting()

    // must be initialized at first
    val mailSub = get<RedisMailSubscriber>()
}