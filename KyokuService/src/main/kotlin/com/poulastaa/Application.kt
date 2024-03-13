package com.poulastaa

import com.poulastaa.plugins.*
import io.ktor.server.application.*
import kotlinx.coroutines.*

val invalidTokenList = ArrayList<String>()

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)

    removeFirstTokenAfter10Minute()
}

fun Application.module() {
    configureKoin(this)
    configureSerialization()
    configureSecurity()
    configureRouting()
    configureDatabase()
    configureSession()
}


private fun removeFirstTokenAfter10Minute() {
    CoroutineScope(Dispatchers.IO).launch {
        while (true) {
            delay(600000L)
            try {
                invalidTokenList.removeAt(0)
            } catch (e: Exception) {
                println(e.message.toString())
            }
        }
    }
}