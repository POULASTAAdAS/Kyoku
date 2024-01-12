package com.example

import com.example.plugins.*
import io.ktor.server.application.*
import kotlinx.coroutines.*

val invalidTokenList = ArrayList<String>()

@OptIn(DelicateCoroutinesApi::class)
fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)

    GlobalScope.launch(Dispatchers.IO) {
        removeFirstTokenAfter10Minute()
    }
}

fun Application.module() {
    configureKoin(this)
    configureSerialization()
    configureSecurity()
    configureRouting()
    configureDatabase()
    configureSession()
}

private suspend fun removeFirstTokenAfter10Minute() {
    while (true) {
        delay(600000L)
        try {
            invalidTokenList.removeAt(0)
        } catch (e: Exception) {
            println(e.message.toString())
        }
    }
}