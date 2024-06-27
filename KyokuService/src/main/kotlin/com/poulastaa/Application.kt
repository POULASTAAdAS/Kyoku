package com.poulastaa

import com.poulastaa.plugins.*
import io.ktor.server.application.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val invalidTokenList = ArrayList<String>()

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)

    removeFirstTokenAfter10Minute()
}

fun Application.module() {
    configureKoin(this)
    configureSerialization()
    configureMonitoring()
    configureSecurity()
    configureDatabase()
    configureSession()
    configureRouting()
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
