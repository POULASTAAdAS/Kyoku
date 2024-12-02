package com.poulastaa.app

import com.poulastaa.app.plugins.configureRouting
import com.poulastaa.app.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
}